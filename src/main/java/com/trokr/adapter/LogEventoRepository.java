package com.trokr.adapter;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogEventoRepository
        extends MongoRepository<LogEvento, String> {

    List<LogEvento> findByTipo(String tipo);

    List<LogEvento> findByNivelAndDataHoraBetweenOrderByDataHoraDesc(
            NivelLog nivel,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}