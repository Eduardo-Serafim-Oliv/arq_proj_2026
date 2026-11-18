package com.trokr.model.state.contraproposta;

import com.trokr.exception.TransicaoInvalidaException;
import com.trokr.model.Proposta;
import com.trokr.model.StatusProposta;
import com.trokr.model.state.contraproposta.EstadoCanceladoContra;
import com.trokr.model.state.contraproposta.EstadoContraProposta;
import com.trokr.model.state.contraproposta.EstadoNegociadoContra;

public class EstadoEmAnalise implements EstadoContraProposta {

    @Override
    public void enviarParaAnalise(Proposta proposta) {
        throw new TransicaoInvalidaException(this.getStatus(), StatusProposta.EM_ANALISE);
    }

    @Override
    public void aceitar(Proposta proposta) {
        proposta.mudarEstadoContraPara(new EstadoNegociadoContra());
    }

    @Override
    public void recusar(Proposta proposta) {
        proposta.mudarEstadoContraPara(new EstadoRecusado());
    }

    @Override
    public void finalizar(Proposta proposta) {
        throw new TransicaoInvalidaException(this.getStatus(), StatusProposta.FINALIZADO_CONTRA);
    }

    @Override
    public void cancelar(Proposta proposta) {
        proposta.mudarEstadoContraPara(new EstadoCanceladoContra());
    }

    @Override
    public StatusProposta getStatus() {
        return StatusProposta.EM_ANALISE;
    }
}