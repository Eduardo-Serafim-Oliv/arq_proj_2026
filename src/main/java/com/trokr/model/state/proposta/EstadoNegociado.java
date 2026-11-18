package com.trokr.model.state.proposta;

import com.trokr.exception.TransicaoInvalidaException;
import com.trokr.model.Proposta;
import com.trokr.model.StatusProposta;

public class EstadoNegociado implements EstadoProposta {

    //Avança para homologação
    @Override
    public void enviarParaHomologacao(Proposta proposta){
        throw new TransicaoInvalidaException(this.getStatus(), StatusProposta.HOMOLOGACAO);
    };

    // Volta para rascunho
    @Override
    public void voltarParaRascunho(Proposta proposta){
        throw new TransicaoInvalidaException(this.getStatus(), StatusProposta.RASCUNHO);
    };

    // Avança para ativar
    @Override
    public void aceitarHomologacao(Proposta proposta){
        throw new TransicaoInvalidaException(this.getStatus(), StatusProposta.ATIVO);
    };

    // Volta para rascunho
    @Override
    public void desativarProposta(Proposta proposta){
        throw new TransicaoInvalidaException(this.getStatus(), StatusProposta.RASCUNHO);
    };

    // Avança para negociação
    @Override
    public void aceitarContraProposta(Proposta proposta){
        throw new TransicaoInvalidaException(this.getStatus(), StatusProposta.NEGOCIADO);

    };

    // Volta para ativa
    @Override
    public void negociacaoFalhou(Proposta proposta){
        proposta.mudarEstadoPara(new EstadoAtivo());
    };

    // Avança para finalizado
    @Override
    public void finalizarNegociacao(Proposta proposta){
        proposta.mudarEstadoPara(new EstadoFinalizado());
    };

    // Avança para cancelado
    @Override
    public void cancelar(Proposta proposta){
        throw new TransicaoInvalidaException(this.getStatus(), StatusProposta.CANCELADO);
    };

    @Override
    public StatusProposta getStatus() {
        return StatusProposta.NEGOCIADO;
    }
}
