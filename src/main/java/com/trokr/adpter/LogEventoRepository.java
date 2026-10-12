package com.trokr.adpter;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogEventoRepository extends MongoRepository<LogEvento, String> {

    // TODO: query method para buscar por tipo (equivalente a findByTipo, igual JPA)

    // TODO: query method para buscar por nivel + intervalo de tempo — útil para "erros nas últimas 24h"
}

