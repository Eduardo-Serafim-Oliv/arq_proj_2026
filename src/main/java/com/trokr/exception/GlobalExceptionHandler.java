package com.trokr.exception;

import java.util.Map;

import com.trokr.service.LogEventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratamento de exceção MUITO simples de propósito nesta fase do curso:
 * só um handler genérico para "não encontrado". Erros de validação
 * (@Valid) já viram 400 automaticamente pelo Spring, sem código extra.
 * Um tratamento de erros padronizado e mais completo é conteúdo de uma
 * aula futura.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LogEventoService eventoService;

    public GlobalExceptionHandler(LogEventoService eventoService) {
        this.eventoService = eventoService;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", ex.getMessage()));
    }

    // no GlobalExceptionHandler que já existe
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String,String>> handleConflito(ObjectOptimisticLockingFailureException ex) {

        String mensagem = "Houve um erro de conflito entre " +
                "requisições. Por favor recarregue a proposta.";


        eventoService.registrarAviso(
                "CONFLITO_DE_REQUISICOES",
                "GlobalExceptionHandler",
                mensagem,
                ex
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensagem", mensagem));

    }
}
