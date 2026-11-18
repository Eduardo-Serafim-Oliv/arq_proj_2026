package com.trokr.notificacao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoSimuladaAdapter implements NotificacaoPort {

    private final NotificacaoProperties props;
    private final SimulacaoNotificacao simulacao;

    @Override
    public void notificar(Long usuarioId, String mensagem) {

        // Verifica se o serviço está fora do ar
        if (simulacao.foraDoAr()) {
            throw new NotificacaoIndisponivelException(
                    "Serviço de notificação fora do ar."
            );
        }

        // Simula a latência
        try {
            Thread.sleep(props.getLatenciaMs());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new NotificacaoIndisponivelException(
                    "Envio da notificação interrompido.", ex
            );
        }

        // Simula a taxa de falha
        double sorteio = ThreadLocalRandom.current().nextDouble();

        if (sorteio < props.getTaxaFalha()) {
            throw new NotificacaoIndisponivelException(
                    "Serviço de notificações temporariamente indisponível."
            );
        }

        log.info(
                "Notificação enviada para o usuário {}: {}",
                usuarioId,
                mensagem
        );
    }
}