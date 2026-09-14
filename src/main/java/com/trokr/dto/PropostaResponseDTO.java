package com.trokr.dto;

import com.trokr.model.Proposta;
import java.time.LocalDateTime;

/**
 * Dados de saída de um Item. Os dados do dono são achatados aqui
 * (usuarioId/usuarioNome) para manter o DTO simples, em vez de criar mais
 * uma classe aninhada só para isso.
 */
public record PropostaResponseDTO(
        Long id,
        String titulo,
        String tipo,
        String descricao,
        Long usuarioId,
        String usuarioNome,
        LocalDateTime dataCriacao
) {

    public static PropostaResponseDTO fromEntity(Proposta proposta) {
        return new PropostaResponseDTO(
                proposta.getId(),
                proposta.getTitulo(),
                proposta.getTipo(),
                proposta.getDescricao(),
                proposta.getUsuarioProprietario().getId(),
                proposta.getUsuarioProprietario().getNome(),
                proposta.getDataCriacao()
        );
    }
}
