package com.trokr.notificacao;

public class NotificacaoIndisponivelException extends RuntimeException {

    public NotificacaoIndisponivelException(String mensagem) {
        super(mensagem);
    }

    public NotificacaoIndisponivelException(
            String mensagem,
            Throwable causa) {
        super(mensagem, causa);
    }
}