package com.trokr.model.state.contraproposta;

import com.trokr.model.Proposta;
import com.trokr.model.StatusProposta;

public class EstadoFinalizadoContra implements EstadoContraProposta {

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
        throw new IllegalStateException("Operação inválida!");
    }

    @Override
    public void finalizar(Proposta proposta) {
        throw new IllegalStateException("Operação inválida!");
    }

    @Override
    public void cancelar(Proposta proposta) {
        throw new IllegalStateException("Operação inválida!");
    }

    @Override
    public StatusProposta getStatus() {
        return StatusProposta.FINALIZADO_CONTRA;
    }
}
