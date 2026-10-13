package com.trokr.event;

import com.trokr.model.Proposta;
import com.trokr.model.Usuario;

import java.time.LocalDateTime;

public class TrocaConcluidaEvent {

    private final Long propostaId;
    private final Usuario usuarioA;
    private final Usuario usuarioB;
    private final Proposta propostaA;
    private final Proposta propostaB;
    private final LocalDateTime dataConclusao;

    public TrocaConcluidaEvent(
            Long propostaId,
            Usuario usuarioA,
            Usuario usuarioB,
            Proposta propostaA,
            Proposta propostaB,
            LocalDateTime dataConclusao
    ) {
        this.propostaId = propostaId;
        this.usuarioA = usuarioA;
        this.usuarioB = usuarioB;
        this.propostaA = propostaA;
        this.propostaB = propostaB;
        this.dataConclusao = dataConclusao;
    }

    public Long getPropostaId() {
        return propostaId;
    }

    public Usuario getUsuarioA() {
        return usuarioA;
    }

    public Usuario getUsuarioB() {
        return usuarioB;
    }

    public Proposta getPropostaA() {
        return propostaA;
    }

    public Proposta getPropostaB() {
        return propostaB;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

// construtor, getters — sem nenhum método de ação
// o evento não SABE o que vai ser feito com ele

}
