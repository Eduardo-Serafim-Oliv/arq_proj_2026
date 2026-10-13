package com.trokr.model;

import com.trokr.model.state.EstadoRecusado;
import com.trokr.model.state.contraproposta.*;
import com.trokr.model.state.contraproposta.EstadoEmAnalise;
import com.trokr.model.state.proposta.*;
import com.trokr.service.PropostaService;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProposta categoria;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuarioProprietario;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProposta status = StatusProposta.RASCUNHO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposta_origem_id")
    private Proposta propostaOrigem;

    @Transient
    private EstadoProposta estadoProposta = new EstadoRascunho();

    @Transient
    private EstadoContraProposta estadoContraProposta;


    public void mudarEstadoPara(EstadoProposta novoEstado) {

        this.estadoProposta = novoEstado;
        this.status = novoEstado.getStatus();
    }

    public void mudarEstadoContraPara(EstadoContraProposta novoEstado) {

        this.estadoContraProposta = novoEstado;
        this.status = novoEstado.getStatus();
    }

    public boolean isContraProposta() {
        return propostaOrigem != null;
    }

    public void configurarComoContraProposta(Proposta propostaOrigem) {

        if (propostaOrigem == null) {
            throw new IllegalArgumentException(
                    "A proposta de origem é obrigatória."
            );
        }

        if (propostaOrigem == this) {
            throw new IllegalArgumentException(
                    "Uma proposta não pode ser contra-proposta dela mesma."
            );
        }

        this.propostaOrigem = propostaOrigem;
        this.estadoProposta = null;
        this.estadoContraProposta = new EstadoRascunhoContra();
        this.status = StatusProposta.RASCUNHO;
    }

    @PostLoad
    private void reconstruirEstado() {

        if (isContraProposta()) {
            reconstruirEstadoContraProposta();
        } else {
            reconstruirEstadoProposta();
        }
    }

    private void reconstruirEstadoProposta() {

        this.estadoContraProposta = null;

        switch (status) {

            case RASCUNHO ->
                    this.estadoProposta = new EstadoRascunho();

            case HOMOLOGACAO ->
                    this.estadoProposta = new EstadoHomologacao();

            case ATIVO ->
                    this.estadoProposta = new EstadoAtivo();

            case NEGOCIADO ->
                    this.estadoProposta = new EstadoNegociado();

            case FINALIZADO ->
                    this.estadoProposta = new EstadoFinalizado();

            case CANCELADO ->
                    this.estadoProposta = new EstadoCancelado();

            default ->
                    throw new IllegalStateException(
                            "Status inválido para uma proposta: " + status
                    );
        }
    }

    private void reconstruirEstadoContraProposta() {

        this.estadoProposta = null;

        switch (status) {

            case RASCUNHO ->
                    this.estadoContraProposta = new EstadoRascunhoContra();

            case EM_ANALISE ->
                    this.estadoContraProposta = new EstadoEmAnalise();

            case NEGOCIADO_CONTRA ->
                    this.estadoContraProposta = new EstadoNegociadoContra();

            case FINALIZADO_CONTRA ->
                    this.estadoContraProposta = new EstadoFinalizadoContra();

            case RECUSADO ->
                    this.estadoContraProposta = new EstadoRecusado();

            case CANCELADO_CONTRA ->
                    this.estadoContraProposta = new EstadoCanceladoContra();

            default ->
                    throw new IllegalStateException(
                            "Status inválido para uma contra-proposta: " + status
                    );
        }
    }


// ===============================
// VALIDAÇÕES
// ===============================

    public EstadoProposta validarPropostaOriginal() {
        if (isContraProposta()) {
            throw new IllegalStateException(
                    "Operação válida somente para proposta original."
            );
        }
        return estadoProposta;
    }

    public EstadoContraProposta validarContraProposta() {
        if (!isContraProposta()) {
            throw new IllegalStateException(
                    "Operação válida somente para contra-proposta."
            );
        }
        return estadoContraProposta;
    }
}