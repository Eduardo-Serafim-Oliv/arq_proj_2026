package com.trokr.repository;

import com.trokr.model.Avaliacao;
import com.trokr.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Uso direto do Spring Data JPA, sem interface/abstração genérica de
// repositório por cima — não há necessidade disso ainda.
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

//    Optional<Usuario> findBy(String email);
//    List<Usuario> findByNomeContainingIgnoreCase(String nome);
}
