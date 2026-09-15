package com.trokr.model;

import com.trokr.model.state.EstadoRecusado;
import com.trokr.model.state.contraproposta.*;
import com.trokr.model.state.contraproposta.EstadoEmAnalise;
import com.trokr.model.state.proposta.*;
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

    //    public boolean isRaiz(){
//        return propostaAnterior == null;
//    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposta_origem_id")
    private Proposta propostaOrigem;

    @Transient
    private EstadoProposta estadoProposta = new EstadoRascunho();

    @Transient
    private EstadoContraProposta estadoContraProposta;

    public StatusProposta getStatus() {
        return status;
    }

    public void mudarEstadoPara(EstadoProposta novoEstado) {

        if (isContraProposta()) {
            throw new IllegalStateException(
                    "Uma contra-proposta não pode utilizar o estado de uma proposta."
            );
        }

        this.estadoProposta = novoEstado;
        this.status = novoEstado.getStatus();
    }

    public void mudarEstadoContraPara(EstadoContraProposta novoEstado) {

        if (!isContraProposta()) {
            throw new IllegalStateException(
                    "Uma proposta não pode utilizar o estado de uma contra-proposta."
            );
        }

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
// ESTADO DA PROPOSTA ORIGINAL
// ===============================

    public void enviarParaHomologacao() {
        validarPropostaOriginal();
        estadoProposta.enviarParaHomologacao(this);
    }

    public void voltarParaRascunho() {
        validarPropostaOriginal();
        estadoProposta.voltarParaRascunho(this);
    }

    public void aceitarHomologacao() {
        validarPropostaOriginal();
        estadoProposta.aceitarHomologacao(this);
    }

    public void desativarProposta() {
        validarPropostaOriginal();
        estadoProposta.desativarProposta(this);
    }

    public void aceitarContraProposta() {
        validarPropostaOriginal();
        estadoProposta.aceitarContraProposta(this);
    }

    public void negociacaoFalhou() {
        validarPropostaOriginal();
        estadoProposta.negociacaoFalhou(this);
    }

    public void finalizarNegociacao() {
        validarPropostaOriginal();
        estadoProposta.finalizarNegociacao(this);
    }

    public void cancelar() {
        validarPropostaOriginal();
        estadoProposta.cancelar(this);
    }


// ===============================
// ESTADO DA CONTRA-PROPOSTA
// ===============================

    public void enviarParaAnalise() {
        validarContraProposta();
        estadoContraProposta.enviarParaAnalise(this);
    }

    public void aceitar() {
        validarContraProposta();
        estadoContraProposta.aceitar(this);
    }

    public void recusar() {
        validarContraProposta();
        estadoContraProposta.recusar(this);
    }

    public void finalizarContra() {
        validarContraProposta();
        estadoContraProposta.finalizar(this);
    }

    public void cancelarContra() {
        validarContraProposta();
        estadoContraProposta.cancelar(this);
    }


// ===============================
// VALIDAÇÕES
// ===============================

    private void validarPropostaOriginal() {
        if (isContraProposta()) {
            throw new IllegalStateException(
                    "Operação válida somente para proposta original."
            );
        }
    }

    private void validarContraProposta() {
        if (!isContraProposta()) {
            throw new IllegalStateException(
                    "Operação válida somente para contra-proposta."
            );
        }
    }
}