package com.trokr.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.trokr.repository.LogEventoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistroDeEventosMongo implements RegistroDeEventos {

    private final LogEventoRepository mongoRepository;

    @Override
    public void registrar(
            String tipo,
            NivelLog nivel,
            String origem,
            String mensagem,
            Map<String, Object> payload
    ) {

        LogEvento documento = new LogEvento();

        documento.setTipo(tipo);
        documento.setNivel(nivel);
        documento.setOrigem(origem);
        documento.setMensagem(mensagem);
        documento.setPayload(payload);
        documento.setDataHora(LocalDateTime.now());

        mongoRepository.insert(documento);
    }

    @Override
    public List<LogEvento> buscarPorTipo(String tipo) {
        return mongoRepository.findByTipo(tipo);
    }

    @Override
    public List<LogEvento> buscarPorNivelEPeriodo(
            NivelLog nivel,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        return mongoRepository
                .findByNivelAndDataHoraBetweenOrderByDataHoraDesc(
                        nivel,
                        inicio,
                        fim
                );
    }
}