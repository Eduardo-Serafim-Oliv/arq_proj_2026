package com.trokr.repository;

import com.trokr.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoRepository
        extends JpaRepository<Historico, Long> {
}