package com.trokr.dto;

import com.trokr.adapter.LogEvento;
import com.trokr.adapter.NivelLog;

import java.time.LocalDateTime;
import java.util.Map;

public record LogEventoResponseDTO(
        String id,
        String tipo,
        NivelLog nivel,
        String origem,
        String mensagem,
        Map<String, Object> payload,
        LocalDateTime dataHora
) {

    public static LogEventoResponseDTO fromEntity(LogEvento evento) {
        return new LogEventoResponseDTO(
                evento.getId(),
                evento.getTipo(),
                evento.getNivel(),
                evento.getOrigem(),
                evento.getMensagem(),
                evento.getPayload(),
                evento.getDataHora()
        );
    }
}