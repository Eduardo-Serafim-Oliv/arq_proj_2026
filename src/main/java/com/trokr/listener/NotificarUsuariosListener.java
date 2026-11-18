package com.trokr.listener;

import com.trokr.event.TrocaConcluidaEvent;
import com.trokr.notificacao.NotificacaoPort;
import com.trokr.service.LogEventoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificarUsuariosListener {

    private final NotificacaoPort notificacaoPort;
    private final LogEventoService logEventoService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoConcluirTroca(TrocaConcluidaEvent evento) {

        String mensagem = "Sua troca da proposta "
                + evento.getPropostaId()
                + " foi concluída.";

        for (Long usuarioId : List.of(
                evento.getUsuarioA().getId(),
                evento.getUsuarioB().getId())) {

            try {
                notificacaoPort.notificar(usuarioId, mensagem);

            } catch (Exception ex) {
                log.error(
                        "Notificação para o usuário {} falhou",
                        usuarioId,
                        ex
                );

                registrarFalha(evento, usuarioId, ex);
            }
        }
    }

    private void registrarFalha(
            TrocaConcluidaEvent evento,
            Long usuarioId,
            Exception ex) {

        String mensagem = String.format(
                "Falha ao enviar notificação para o usuário %d "
                        + "após a conclusão da proposta %d.",
                usuarioId,
                evento.getPropostaId()
        );

        try {
            logEventoService.registrarErro(
                    "FALHA_ENVIO_NOTIFICACAO",
                    "NotificarUsuariosListener",
                    mensagem,
                    ex
            );

        } catch (Exception erroRegistro) {
            log.error(
                    "Não foi possível registrar a falha de notificação "
                            + "para o usuário {}.",
                    usuarioId,
                    erroRegistro
            );
        }
    }

    // Mantenha aqui sua implementação existente de registrarFalha().
}