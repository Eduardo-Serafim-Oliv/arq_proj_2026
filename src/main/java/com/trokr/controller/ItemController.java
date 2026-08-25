package com.trokr.controller;

import com.trokr.dto.ItemRequestDTO;
import com.trokr.dto.ItemResponseDTO;
import com.trokr.model.Item;
import com.trokr.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itens")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemResponseDTO> listar() {
        return itemService.listarTodos().stream()
                .map(ItemResponseDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ItemResponseDTO buscarPorId(@PathVariable Long id) {
        return ItemResponseDTO.fromEntity(itemService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public List<ItemResponseDTO> listarPorTitulo(@RequestParam("titulo") String titulo){
        return itemService.listarPorTitulo(titulo).stream()
                .map(ItemResponseDTO::fromEntity)
                .toList();
    }

    @GetMapping("/buscar-tipo")
    public List<ItemResponseDTO> listarPorTipo(@RequestParam("tipo") String tipo){
        return itemService.listarPorTipo(tipo).stream()
                .map(ItemResponseDTO::fromEntity)
                .toList();
    }

    @GetMapping("/buscar-itens-usuario/{id}")
    public List<ItemResponseDTO> listarPorProprietario(@PathVariable Long id){
        return itemService.listarPorUsuarioProprietario(id).stream()
                .map(ItemResponseDTO::fromEntity)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> criar(@Valid @RequestBody ItemRequestDTO dto) {
        Item item = new Item();
        item.setTitulo(dto.titulo());
        item.setDescricao(dto.descricao());
        item.setTipo(dto.tipo());

        Item salvo = itemService.criar(item, dto.usuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ItemResponseDTO.fromEntity(salvo));
    }

    @PutMapping("/{id}")
    public ItemResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody ItemRequestDTO dto) {
        Item dadosAtualizados = new Item();
        dadosAtualizados.setTitulo(dto.titulo());
        dadosAtualizados.setDescricao(dto.descricao());
        dadosAtualizados.setTipo(dto.tipo());

        return ItemResponseDTO.fromEntity(itemService.atualizar(id, dadosAtualizados, dto.usuarioId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        itemService.remover(id);
        return ResponseEntity.noContent().build();
    }

}
