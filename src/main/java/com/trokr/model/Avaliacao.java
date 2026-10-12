package com.trokr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_avaliador_id", nullable = false)
    private Usuario usuarioAvaliador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_avaliado_id", nullable = false)
    private Usuario usuarioAvaliado;

    @Column
    private Integer nota;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAvaliacao status;

    @Column
    private String descricao;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
}