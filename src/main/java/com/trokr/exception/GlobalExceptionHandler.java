package com.trokr.exception;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.trokr.service.LogEventoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Tratamento de exceção MUITO simples de propósito nesta fase do curso:
 * só um handler genérico para "não encontrado". Erros de validação
 * (@Valid) já viram 400 automaticamente pelo Spring, sem código extra.
 * Um tratamento de erros padronizado e mais completo é conteúdo de uma
 * aula futura.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final LogEventoService eventoService;

    public GlobalExceptionHandler(LogEventoService eventoService) {
        this.eventoService = eventoService;
    }

//    Método para montagem de um ProblemDetail
    private ProblemDetail problema(HttpStatus status, String titulo, String codigo,
                                   String detalhe, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detalhe);
        pd.setTitle(titulo);
        pd.setType(URI.create("https://trokr/erros/" + codigo.toLowerCase()));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("codigo", codigo);
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

//   handler de dominio para exceptions específicos que herdam de TrokrException
    @ExceptionHandler(TrokrException.class)
    public ProblemDetail dominio(TrokrException ex, HttpServletRequest req) {
        HttpStatus status = switch (ex) {
            case RecursoNaoEncontradoException e -> HttpStatus.NOT_FOUND;
            case TransicaoInvalidaException e -> HttpStatus.CONFLICT;
            case RegraNegocioException e -> HttpStatus.UNPROCESSABLE_ENTITY;
        };
        return problema(status, status.getReasonPhrase(), ex.getCodigo(), ex.getMessage(), req);
    }

//Se eu quiser fazer cada handler diretamente, sem dominio:

//    @ExceptionHandler(RecursoNaoEncontradoException.class)
//    public ProblemDetail esgotado(RecursoNaoEncontradoException ex, HttpServletRequest req) {
//        return problema(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getCodigo(), ex.getMessage(), req);
//    }

//   handler para argumentos inválidos em requisições
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        HttpServletRequest servletRequest =
                ((ServletWebRequest) request).getRequest();

        ProblemDetail pd = problema(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "VALIDACAO",
                "Um ou mais campos possuem valores inválidos.",
                servletRequest
        );

        Map<String, String> campos = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erro ->
                campos.put(erro.getField(), erro.getDefaultMessage())
        );

        pd.setProperty("campos", campos);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(pd);
    }

    // TODO: os @ExceptionHandler das linhas da tabela anterior

    // handler para o erro de conflito entre requisições concorrentes
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handlerConflito(ObjectOptimisticLockingFailureException ex, WebRequest request) {

        HttpServletRequest servletRequest =
                ((ServletWebRequest) request).getRequest();

        String mensagem = "Houve um erro de conflito entre " +
                "requisições. Por favor recarregue a proposta.";


        eventoService.registrarAviso(
                "CONFLITO_DE_REQUISICOES",
                "GlobalExceptionHandler",
                mensagem,
                ex
        );

        return problema(HttpStatus.CONFLICT, "Conflito entre requisições", "PROPOSTA_ALTERADA_CONCORRENTEMENTE", mensagem, servletRequest);
    }


    //handler para erros internos ou que não tenham sido tratados ainda por erros específicos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleErroInterno(
            Exception ex,
            WebRequest request) {

        String ocorrencia = UUID.randomUUID().toString();

        HttpServletRequest servletRequest =
                ((ServletWebRequest) request).getRequest();

        ProblemDetail pd = problema(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "ERRO_INTERNO",
                "Ocorreu um erro inesperado.",
                servletRequest
        );

        pd.setProperty("ocorrencia", ocorrencia);

        // Registra o erro internamente
        log.error(
                "Erro interno. Ocorrência: {}",
                ocorrencia,
                ex
        );

        try {

            eventoService.registrarErro(
                    "ERRO_INTERNO",
                    "GlobalExceptionHandler",
                "Ocorrência: " + ocorrencia,
                    ex
            );

        } catch (Exception erroRegistro) {
            log.error(
                    "Não foi possível registrar o erro interno no banco. Ocorrência: {}",
                    ocorrencia,
                    erroRegistro
            );
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(pd);
    }
}
