package com.trokr.notificacao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NotificacaoComRetry implements NotificacaoPort {

    private final NotificacaoPort proximo;
    private final int maxTentativas;
    private final long esperaInicialMs;

    @Override
    public void notificar(Long usuarioId, String mensagem) {

        long espera = esperaInicialMs;

        for (int tentativa = 1; tentativa <= maxTentativas; tentativa++) {

            try {
                proximo.notificar(usuarioId, mensagem);
                return;

            } catch (NotificacaoIndisponivelException ex) {

                log.warn(
                        "Tentativa {}/{} para o usuário {} falhou: {}",
                        tentativa,
                        maxTentativas,
                        usuarioId,
                        ex.getMessage()
                );

                if (tentativa == maxTentativas) {
                    throw ex;
                }

                dormir(espera);
                espera *= 2;
            }
        }
    }

    private void dormir(long ms) {
        try {
            Thread.sleep(ms);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();

            throw new NotificacaoIndisponivelException(
                    "Retry da notificação interrompido.",
                    ex
            );
        }
    }
}