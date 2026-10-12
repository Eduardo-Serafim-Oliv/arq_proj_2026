package com.trokr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposta_id", nullable = false)
    private Proposta proposta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProposta statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProposta statusNovo;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime data;
}