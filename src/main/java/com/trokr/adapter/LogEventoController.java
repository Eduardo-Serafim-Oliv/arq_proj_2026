package com.trokr.controller;

import com.trokr.adapter.LogEventoService;
import com.trokr.dto.LogEventoResponseDTO;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogEventoController {

    private final LogEventoService logEventoService;

    // Um jeito de consultar o log, sem abrir o Compass toda vez
    // ============================================================

    @GetMapping
    public List<LogEventoResponseDTO> listarPorTipo(
            @RequestParam String tipo
    ) {
        return logEventoService.listarPorTipo(tipo)
                .stream()
                .map(LogEventoResponseDTO::fromEntity)
                .toList();
    }
}