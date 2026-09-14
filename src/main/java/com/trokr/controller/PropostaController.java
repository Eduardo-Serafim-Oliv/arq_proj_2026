package com.trokr.controller;

import com.trokr.dto.PropostaRequestDTO;
import com.trokr.dto.PropostaResponseDTO;
import com.trokr.model.Proposta;
import com.trokr.service.PropostaService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/propostas")
@RequiredArgsConstructor
public class PropostaController {

    private final PropostaService propostaService;

    @GetMapping
    public List<PropostaResponseDTO> listar() {
        return propostaService.listarTodos().stream()
                .map(PropostaResponseDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public PropostaResponseDTO buscarPorId(@PathVariable Long id) {
        return PropostaResponseDTO.fromEntity(
                propostaService.buscarPorId(id)
        );
    }

    @GetMapping("/buscar")
    public List<PropostaResponseDTO> listarPorTitulo(
            @RequestParam("titulo") String titulo) {

        return propostaService.listarPorTitulo(titulo).stream()
                .map(PropostaResponseDTO::fromEntity)
                .toList();
    }

    @GetMapping("/buscar-tipo")
    public List<PropostaResponseDTO> listarPorTipo(
            @RequestParam("tipo") String tipo) {

        return propostaService.listarPorTipo(tipo).stream()
                .map(PropostaResponseDTO::fromEntity)
                .toList();
    }

    @GetMapping("/buscar-propostas-usuario/{id}")
    public List<PropostaResponseDTO> listarPorUsuario(
            @PathVariable Long id) {

        return propostaService.listarPorUsuarioProprietario(id).stream()
                .map(PropostaResponseDTO::fromEntity)
                .toList();
    }

    @PostMapping
    public ResponseEntity<PropostaResponseDTO> criar(
            @Valid @RequestBody PropostaRequestDTO dto) {

        Proposta proposta = new Proposta();

        proposta.setTitulo(dto.titulo());
        proposta.setDescricao(dto.descricao());
        proposta.setTipo(dto.tipo());

        Proposta salvo =
                propostaService.criar(proposta, dto.usuarioId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PropostaResponseDTO.fromEntity(salvo));
    }

    @PutMapping("/{id}")
    public PropostaResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PropostaRequestDTO dto) {

        Proposta dadosAtualizados = new Proposta();

        dadosAtualizados.setTitulo(dto.titulo());
        dadosAtualizados.setDescricao(dto.descricao());
        dadosAtualizados.setTipo(dto.tipo());

        return PropostaResponseDTO.fromEntity(
                propostaService.atualizar(
                        id,
                        dadosAtualizados,
                        dto.usuarioId()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {

        propostaService.remover(id);

        return ResponseEntity.noContent().build();
    }
}