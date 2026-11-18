package com.trokr.model.state.contraproposta;

import com.trokr.exception.TransicaoInvalidaException;
import com.trokr.model.Proposta;
import com.trokr.model.StatusProposta;
import com.trokr.model.state.proposta.EstadoProposta;

public class EstadoRascunhoContra implements EstadoContraProposta {

    @Override
    public void enviarParaAnalise(Proposta proposta) {
        proposta.mudarEstadoContraPara(new EstadoEmAnalise());
    }

    @Override
    public void aceitar(Proposta proposta) {
        throw new TransicaoInvalidaException(this.getStatus(), StatusProposta.NEGOCIADO_CONTRA);
    }

    @Override
    public void recusar(Proposta proposta) {
        throw new TransicaoInvalidaException(this.getStatus(), StatusProposta.RECUSADO);
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
        return StatusProposta.RASCUNHO;
    }

}
