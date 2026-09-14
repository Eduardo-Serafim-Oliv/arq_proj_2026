package com.trokr.repository;

import com.trokr.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    List<Proposta> findByTituloContainingIgnoreCase(String titulo);
    List<Proposta> findByTipoIgnoreCase(String tipo);
    List<Proposta> findByUsuarioProprietarioId(Long usuarioId);

}
