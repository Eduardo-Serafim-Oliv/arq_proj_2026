package com.trokr.service;

import com.trokr.dto.PropostaRequestDTO;
import com.trokr.exception.ResourceNotFoundException;
import com.trokr.model.Proposta;
import com.trokr.model.StatusProposta;
import com.trokr.model.Usuario;
import com.trokr.model.state.contraproposta.EstadoRascunhoContra;
import com.trokr.repository.PropostaRepository;
import java.util.List;

import com.trokr.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropostaService {

    private final PropostaRepository propostaRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public List<Proposta> listarTodos() {
        return propostaRepository.findAll();
    }

    public Proposta buscarPorId(Long id) {
        return propostaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com id " + id));
    }

    public List<Proposta> listarPorTitulo(String titulo) {
        return propostaRepository.findByTituloContainingIgnoreCase(titulo);
    }
    public List<Proposta> listarPorTipo(String tipo) {
        return propostaRepository.findByTipoIgnoreCase(tipo);
    }
    public List<Proposta> listarPorUsuarioProprietario(Long usuarioId) {
        return propostaRepository.findByUsuarioProprietarioId(usuarioId);
    }

    public Proposta criar(Proposta proposta, Long usuarioId) {
        Usuario dono = usuarioService.buscarPorId(usuarioId);
        proposta.setUsuarioProprietario(dono);
        return propostaRepository.save(proposta);
    }

    public Proposta atualizar(Long id, Proposta dadosAtualizados, Long usuarioId) {
        Proposta propostaExistente = buscarPorId(id);
        Usuario dono = usuarioService.buscarPorId(usuarioId);
        propostaExistente.setTitulo(dadosAtualizados.getTitulo());
        propostaExistente.setDescricao(dadosAtualizados.getDescricao());
        propostaExistente.setTipo(dadosAtualizados.getTipo());
        propostaExistente.setStatus(dadosAtualizados.getStatus());
        propostaExistente.setUsuarioProprietario(dono);
        return propostaRepository.save(propostaExistente);
    }

    public void remover(Long id) {
        Proposta proposta = buscarPorId(id);
        propostaRepository.delete(proposta);
    }

//    public Long ContarItens() {
//        return itemRepository.count();
//    }

    @Transactional
    public Proposta criarContraProposta(
            Long propostaOrigemId,
            PropostaRequestDTO dto) {

        Proposta propostaOrigem = propostaRepository
                .findById(propostaOrigemId)
                .orElseThrow(() ->
                        new RuntimeException("Proposta não encontrada")
                );

        Usuario usuario = usuarioRepository
                .findById(dto.usuarioId())
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado")
                );

        if (propostaOrigem.getUsuarioProprietario().getId()
                .equals(usuario.getId())) {

            throw new IllegalStateException(
                    "O dono da proposta não pode criar uma contra-proposta."
            );
        }

        Proposta contraProposta = new Proposta();

        contraProposta.setTitulo(dto.titulo());
        contraProposta.setDescricao(dto.descricao());
        contraProposta.setTipo(dto.tipo());
        contraProposta.setUsuarioProprietario(usuario);

        contraProposta.configurarComoContraProposta(propostaOrigem);

        return propostaRepository.save(contraProposta);
    }

    public Proposta buscarContraProposta(
            Long propostaId,
            Long contraId) {

        return propostaRepository
                .findByIdAndPropostaOrigemId(contraId, propostaId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Contra-proposta não encontrada."
                        )
                );
    }

    @Transactional
    public Proposta aceitarContraProposta(
            Long propostaId,
            Long contraId) {

        // Busca a proposta original
        Proposta proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() ->
                        new RuntimeException("Proposta não encontrada")
                );

        // Busca a contra-proposta e garante que ela pertence
        // à proposta original informada
        Proposta contraProposta = propostaRepository
                .findByIdAndPropostaOrigemId(contraId, propostaId)
                .orElseThrow(() ->
                        new RuntimeException("Contra-proposta não encontrada")
                );

        // Aceita a contra-proposta
        contraProposta.aceitar();

        // Coloca a proposta original em negociação
        proposta.aceitarContraProposta();

        // Busca todas as contra-propostas da proposta original
        List<Proposta> contraPropostas =
                propostaRepository.findByPropostaOrigemId(propostaId);

        // Remove todas as outras
        for (Proposta contra : contraPropostas) {

            if (!contra.getId().equals(contraId)) {
                contra.recusar();
//                ou se for deletar --> PropostaRepository.delete(contra);
            }
        }

        // Salva as alterações
        propostaRepository.save(proposta);
        propostaRepository.save(contraProposta);

        return contraProposta;
    }

    @Transactional
    public List<Proposta> finalizarNegociacao(
            Long propostaId,
            Long contraId) {

        // Busca a proposta original
        Proposta proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() ->
                        new RuntimeException("Proposta não encontrada")
                );

        // Busca a contra-proposta e garante que ela pertence
        // à proposta original informada
        Proposta contraProposta = propostaRepository
                .findByIdAndPropostaOrigemId(contraId, propostaId)
                .orElseThrow(() ->
                        new RuntimeException("Contra-proposta não encontrada ou não pertence à proposta informada.")
                );

        // Finaliza a contra-proposta
        contraProposta.finalizarContra();

        // Finaliza a proposta original
        proposta.finalizarNegociacao();

        // Salva as alterações
        propostaRepository.save(proposta);
        propostaRepository.save(contraProposta);

        return List.of(proposta, contraProposta);
    }

}
