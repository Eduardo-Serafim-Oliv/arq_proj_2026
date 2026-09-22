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
    private final PropostaRepository propostaRepository;

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
    public ResponseEntity<PropostaResponseDTO> criarProposta(
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
        dadosAtualizados.setTipo(dto.tipo());
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

        Proposta proposta = propostaService.buscarPorId(id);

        proposta.enviarParaHomologacao();

        propostaRepository.save(proposta);

        return PropostaResponseDTO.fromEntity(proposta);
    }

    @PatchMapping("/{id}/ativar-proposta")
    public PropostaResponseDTO aceitarHomologacao(
            @PathVariable Long id) {

        Proposta proposta = propostaService.buscarPorId(id);

        proposta.aceitarHomologacao();

        propostaRepository.save(proposta);

        return PropostaResponseDTO.fromEntity(proposta);
    }

    @PatchMapping("/{id}/retornar-para-rascunho")
    public PropostaResponseDTO voltarParaRascunho(
            @PathVariable Long id) {

        Proposta proposta = propostaService.buscarPorId(id);
        proposta.voltarParaRascunho();
        propostaRepository.save(proposta);

        return PropostaResponseDTO.fromEntity(proposta);
    }

    @PatchMapping("/{id}/desativar-proposta")
    public PropostaResponseDTO desativarProposta(
            @PathVariable Long id) {

        Proposta proposta = propostaService.buscarPorId(id);

        proposta.desativarProposta();

        propostaRepository.save(proposta);

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

        Proposta contra = propostaService.buscarContraProposta(
                propostaId,
                contraId
        );

        contra.enviarParaAnalise();

        return PropostaResponseDTO.fromEntity(
                propostaRepository.save(contra)
        );
    }

    @PatchMapping("/{propostaId}/contra-propostas/{contraId}/recusar")
    public PropostaResponseDTO recusarProposta(
            @PathVariable Long propostaId,
            @PathVariable Long contraId) {

        Proposta proposta = propostaService.buscarPorId(propostaId);

        Proposta contra = propostaService.buscarContraProposta(
                propostaId,
                contraId
        );

        proposta.negociacaoFalhou();
        contra.recusar();

        return PropostaResponseDTO.fromEntity(
                propostaRepository.save(contra)
        );
    }

    @PatchMapping("/{id}/cancelar-proposta")
    public PropostaResponseDTO cancelarProposta(
            @PathVariable Long id) {

        Proposta proposta = propostaService.buscarPorId(id);
        proposta.cancelar();
        propostaRepository.save(proposta);

        return PropostaResponseDTO.fromEntity(proposta);
    }

    @PatchMapping("/{propostaId}/contra-propostas/{contraId}/cancelar")
    public PropostaResponseDTO cancelarContraProposta(
            @PathVariable Long propostaId,
            @PathVariable Long contraId) {

        Proposta contraProposta = propostaService.buscarContraProposta(propostaId, contraId);
        contraProposta.cancelarContra();
        propostaRepository.save(contraProposta);

        return PropostaResponseDTO.fromEntity(contraProposta);
    }


}