package com.trokr.model;

import com.trokr.model.state.EstadoProposta;
import com.trokr.model.state.EstadoRascunho;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "proposta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuarioProprietario;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProposta status = StatusProposta.RASCUNHO;

    @Transient
    private EstadoProposta estadoAtual = new EstadoRascunho();

    public void mudarEstadoPara(EstadoProposta novoEstado) {
        this.estadoAtual = novoEstado;
        this.status = novoEstado.getStatus();
    }

    public void enviarParaHomologacao() {
        this.estadoAtual.enviarParaHomologacao(this);
    }

    public void voltarParaRascunho() {
        this.estadoAtual.voltarParaRascunho(this);
    }

    public void aceitarHomologacao() {
        this.estadoAtual.aceitarHomologacao(this);
    }

    public void desativarProposta() {
        this.estadoAtual.desativarProposta(this);
    }

    public void aceitarContraProposta() {
        this.estadoAtual.aceitarContraProposta(this);
    }

    public void negociacaoFalhou() {
        this.estadoAtual.negociacaoFalhou(this);
    }

    public void finalizarNegociacao() {
        this.estadoAtual.finalizarNegociacao(this);
    }

    public void cancelar() {
        this.estadoAtual.cancelar(this);
    }
}