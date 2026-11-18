package com.trokr.exception;

public final class RegraNegocioException extends TrokrException {


//    TODO: Colocar RegraNegocioException para tentativa de trocas com itens que pertencem a outro usuario no propostaService.
//      "ITEM_DE_OUTRO_USUARIO"   --->   "O ítem pertence a outro usuário "

    public RegraNegocioException(String codigo, String mensagem) {
        super(codigo, mensagem);
    }
}
