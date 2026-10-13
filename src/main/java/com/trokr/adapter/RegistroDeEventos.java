package com.trokr.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RegistroDeEventos {

    void registrar(
            String tipo,
            NivelLog nivel,
            String origem,
            String mensagem,
            Map<String, Object> payload
    );

    List<LogEvento> buscarPorTipo(String tipo);

    List<LogEvento> buscarPorNivelEPeriodo(
            NivelLog nivel,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}