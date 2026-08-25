package com.trokr.repository;

import com.trokr.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByTituloContainingIgnoreCase(String titulo);
    List<Item> findByTipoIgnoreCase(String tipo);
    List<Item> findByUsuarioProprietarioId(Long usuarioId);

}
