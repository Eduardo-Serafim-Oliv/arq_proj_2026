package com.trokr.controller;

import com.trokr.dto.AvaliacaoRequestDTO;
import com.trokr.dto.AvaliacaoResponseDTO;
import com.trokr.model.Avaliacao;
import com.trokr.model.StatusAvaliacao;
import com.trokr.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PatchMapping("/{id}")
    public AvaliacaoResponseDTO avaliar(@PathVariable Long id, @Valid @RequestBody AvaliacaoRequestDTO dto)
    {
        Avaliacao avaliacao = avaliacaoService.buscarPorId(id);
        if (avaliacao.getStatus() != StatusAvaliacao.PENDENTE) {
            throw new IllegalStateException("Já avaliada");
        }
        avaliacao.setNota(dto.nota());
        avaliacao.setDescricao(dto.descricao());
        avaliacao.setStatus(StatusAvaliacao.AVALIADA);
        return AvaliacaoResponseDTO.fromEntity(avaliacaoService.salvar(avaliacao));
    }
}
