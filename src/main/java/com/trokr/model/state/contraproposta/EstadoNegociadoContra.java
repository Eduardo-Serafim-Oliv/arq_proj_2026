package com.trokr.model.state.contraproposta;

import com.trokr.model.Proposta;
import com.trokr.model.StatusProposta;
import com.trokr.model.state.EstadoRecusado;

public class EstadoNegociadoContra implements EstadoContraProposta {

    @Override
    public void enviarParaAnalise(Proposta proposta) {
        throw new IllegalStateException("Operação inválida!");
    }

    @Override
    public void aceitar(Proposta proposta) {
        throw new IllegalStateException("Operação inválida!");
    }

    @Override
    public void recusar(Proposta proposta) {
        proposta.mudarEstadoContraPara(new EstadoRecusado());
    }

    @Override
    public void finalizar(Proposta proposta) {
        proposta.mudarEstadoContraPara(new EstadoFinalizadoContra());
    }

    @Override
    public void cancelar(Proposta proposta) {
        throw new IllegalStateException("Operação inválida!");
    }

    @Override
    public StatusProposta getStatus() {
        return StatusProposta.NEGOCIADO_CONTRA;
    }
}
