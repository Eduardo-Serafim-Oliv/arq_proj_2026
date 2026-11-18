package com.trokr.exception;

public abstract sealed class TrokrException extends RuntimeException
        permits RecursoNaoEncontradoException, RegraNegocioException,
        TransicaoInvalidaException {

    private final String codigo;

    protected TrokrException(String codigo, String mensagem) {
        super(mensagem);
        this.codigo = codigo;
    }

    public String getCodigo() { return codigo; }
}
