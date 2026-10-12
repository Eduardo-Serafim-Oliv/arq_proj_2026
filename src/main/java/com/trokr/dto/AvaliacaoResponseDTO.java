package com.trokr.dto;

import com.trokr.model.Avaliacao;
import com.trokr.model.StatusAvaliacao;

import java.time.LocalDateTime;

public record AvaliacaoResponseDTO(
        Long id,
        Long usuarioAvaliadorId,
        String usuarioAvaliadorNome,
        Long usuarioAvaliadoId,
        String usuarioAvaliadoNome,
        Integer nota,
        StatusAvaliacao status,
        String descricao,
        LocalDateTime dataCriacao
) {

    public static AvaliacaoResponseDTO fromEntity(Avaliacao avaliacao) {

        return new AvaliacaoResponseDTO(
                avaliacao.getId(),
                avaliacao.getUsuarioAvaliador().getId(),
                avaliacao.getUsuarioAvaliador().getNome(),
                avaliacao.getUsuarioAvaliado().getId(),
                avaliacao.getUsuarioAvaliado().getNome(),
                avaliacao.getNota(),
                avaliacao.getStatus(),
                avaliacao.getDescricao(),
                avaliacao.getDataCriacao()
        );
    }
}