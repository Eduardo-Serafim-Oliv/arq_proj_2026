package com.trokr.controller;

import com.trokr.dto.PropostaRequestDTO;
import com.trokr.dto.PropostaResponseDTO;
import com.trokr.model.Proposta;
import com.trokr.repository.PropostaRepository;
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

    @GetMapping("/buscar-categoria")
    public List<PropostaResponseDTO> listarPorCategoria(
            @RequestParam("categoria") String categoria) {

        return propostaService.listarPorCategoria(categoria).stream()
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
    public ResponseEntity<PropostaResponseDTO> criarProposta(
            @Valid @RequestBody PropostaRequestDTO dto) {

        Proposta proposta = new Proposta();

        proposta.setTitulo(dto.titulo());
        proposta.setDescricao(dto.descricao());
        proposta.setCategoria(dto.categoria());

        Proposta salvo =
                propostaService.criar(proposta, dto.usuarioId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PropostaResponseDTO.fromEntity(salvo));
    }

    @PostMapping("/{id}")
    public ResponseEntity<PropostaResponseDTO> criarContraProposta(
            @PathVariable Long id,
            @Valid @RequestBody PropostaRequestDTO dto) {

        Proposta contraProposta =
                propostaService.criarContraProposta(id, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PropostaResponseDTO.fromEntity(contraProposta));
    }

    @PutMapping("/{id}")
    public PropostaResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PropostaRequestDTO dto) {

        Proposta dadosAtualizados = new Proposta();

        dadosAtualizados.setTitulo(dto.titulo());
        dadosAtualizados.setDescricao(dto.descricao());
        dadosAtualizados.setCategoria(dto.categoria());
        dadosAtualizados.setStatus(dto.status());

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

    @PatchMapping("/{id}/homologacao")
    public PropostaResponseDTO enviarParaHomologacao(
            @PathVariable Long id) {

        Proposta proposta = propostaService.enviarParaHomologacao(id);
        return PropostaResponseDTO.fromEntity(proposta);
    }

    @PatchMapping("/{id}/ativar-proposta")
    public PropostaResponseDTO aceitarHomologacao(
            @PathVariable Long id) {

        Proposta proposta = propostaService.aceitarHomologacao(id);
        return PropostaResponseDTO.fromEntity(proposta);

    }

    @PatchMapping("/{id}/retornar-para-rascunho")
    public PropostaResponseDTO voltarParaRascunho(
            @PathVariable Long propostaId) {

        Proposta proposta = propostaService.voltarParaRascunho(propostaId);
        return PropostaResponseDTO.fromEntity(proposta);
    }

    @PatchMapping("/{id}/desativar-proposta")
    public PropostaResponseDTO desativarProposta(
            @PathVariable Long id) {

        Proposta proposta = propostaService.desativarProposta(id);
        return PropostaResponseDTO.fromEntity(proposta);
    }

    @PatchMapping("/{propostaId}/contra-propostas/{contraId}/aceitar")
    public PropostaResponseDTO aceitarContraProposta(
            @PathVariable Long propostaId,
            @PathVariable Long contraId) {

        Proposta contraProposta = propostaService.aceitarContraProposta(
                propostaId,
                contraId
        );

        return PropostaResponseDTO.fromEntity(contraProposta);
    }

    @PatchMapping("/{propostaId}/contra-propostas/{contraId}/finalizar")
    public List<PropostaResponseDTO> finalizarNegociacao(
            @PathVariable Long propostaId,
            @PathVariable Long contraId) {

        List<Proposta> propostas = propostaService.finalizarNegociacao(
                propostaId,
                contraId
        );

        return propostas.stream()
                .map(PropostaResponseDTO::fromEntity)
                .toList();
    }

    @PatchMapping("/{propostaId}/contra-propostas/{contraId}/enviar-a-analise")
    public PropostaResponseDTO enviarParaAnalise(
            @PathVariable Long propostaId,
            @PathVariable Long contraId) {

        Proposta contraProposta = propostaService.enviarParaAnalise(
                propostaId,
                contraId
        );

        return PropostaResponseDTO.fromEntity(contraProposta);
    }

    @PatchMapping("/{propostaId}/contra-propostas/{contraId}/recusar")
    public PropostaResponseDTO recusarProposta(
            @PathVariable Long propostaId,
            @PathVariable Long contraId) {

        Proposta contraProposta = propostaService.recusarProposta(propostaId, contraId);
        return PropostaResponseDTO.fromEntity(contraProposta);
    }

    @PatchMapping("/{id}/cancelar-proposta")
    public PropostaResponseDTO cancelarProposta(
            @PathVariable Long id) {

        Proposta proposta = propostaService.cancelarProposta(id);
        return PropostaResponseDTO.fromEntity(proposta);
    }

    @PatchMapping("/{propostaId}/contra-propostas/{contraId}/cancelar")
    public PropostaResponseDTO cancelarContraProposta(
            @PathVariable Long propostaId,
            @PathVariable Long contraId) {

        Proposta contraProposta = propostaService.cancelarContraProposta(propostaId, contraId);
        return PropostaResponseDTO.fromEntity(contraProposta);
    }


}