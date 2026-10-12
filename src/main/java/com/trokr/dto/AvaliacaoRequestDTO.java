package com.trokr.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AvaliacaoRequestDTO(

        @NotNull(message = "nota é obrigatória")
        Integer nota,
        String descricao

) {
}