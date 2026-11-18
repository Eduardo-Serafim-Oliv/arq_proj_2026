package com.trokr.exception;

import com.trokr.model.StatusProposta;

public final class TransicaoInvalidaException extends TrokrException {

    public TransicaoInvalidaException(StatusProposta estadoAtual, StatusProposta estadoRequerido) {
        super("PROPOSTA_TRANSICAO_INVALIDA",
                "Operação inválida! Não é permito transitar de " + estadoAtual.getDescricao() + " para " + estadoRequerido.getDescricao());
    }
}
