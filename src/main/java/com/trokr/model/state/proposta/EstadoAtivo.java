package com.trokr.model.state.proposta;

import com.trokr.model.Proposta;
import com.trokr.model.StatusProposta;

public class EstadoAtivo implements EstadoProposta {

    //Avança para homologação
    @Override
    public void enviarParaHomologacao(Proposta proposta){
        throw new IllegalStateException("Operação inválida!");
    };

    // Volta para rascunho
    @Override
    public void voltarParaRascunho(Proposta proposta){
        proposta.mudarEstadoPara(new EstadoRascunho());
    };

    // Avança para ativar
    @Override
    public void aceitarHomologacao(Proposta proposta){
        throw new IllegalStateException("Operação inválida!");
    };

    // Volta para rascunho
    @Override
    public void desativarProposta(Proposta proposta){
        proposta.mudarEstadoPara(new EstadoRascunho());
    };

    // Avança para negociação
    @Override
    public void aceitarContraProposta(Proposta proposta){
        proposta.mudarEstadoPara(new EstadoNegociado());
    };

    // Volta para ativa
    @Override
    public void negociacaoFalhou(Proposta proposta){
        throw new IllegalStateException("Operação inválida!");

    };

    // Avança para finalizado
    @Override
    public void finalizarNegociacao(Proposta proposta){
        throw new IllegalStateException("Operação inválida!");

    };

    // Avança para cancelado
    @Override
    public void cancelar(Proposta proposta){
        proposta.mudarEstadoPara(new EstadoCancelado());
    };

    @Override
    public StatusProposta getStatus() {
        return StatusProposta.ATIVO;
    }
}
