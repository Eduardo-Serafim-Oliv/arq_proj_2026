package com.trokr.repository;

import com.trokr.model.CategoriaProposta;
import com.trokr.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    List<Proposta> findByTituloContainingIgnoreCase(String titulo);
    List<Proposta> findByCategoriaIgnoreCase(String categoria);
    List<Proposta> findByUsuarioProprietarioId(Long usuarioId);
    List<Proposta> findByPropostaOrigemId(Long propostaOrigemId);
    Optional<Proposta> findByIdAndPropostaOrigemId(
            Long id,
            Long propostaOrigemId
    );

}
