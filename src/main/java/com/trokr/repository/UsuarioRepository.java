package com.trokr.repository;

import com.trokr.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

// Uso direto do Spring Data JPA, sem interface/abstração genérica de
// repositório por cima — não há necessidade disso ainda.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    @Modifying
    @Query("""
        update Usuario u
        set u.saldoCreditos = u.saldoCreditos + :valor
        where u.id = :id
    """)
    int adicionarCreditos(Long id, int valor);
}
