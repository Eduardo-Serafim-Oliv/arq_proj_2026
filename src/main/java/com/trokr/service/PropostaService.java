package com.trokr.service;

import com.trokr.dto.PropostaRequestDTO;
import com.trokr.exception.ResourceNotFoundException;
import com.trokr.model.Proposta;
import com.trokr.model.Usuario;
import com.trokr.event.TrocaConcluidaEvent;
import com.trokr.repository.PropostaRepository;

import java.time.LocalDateTime;
import java.util.List;

import com.trokr.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropostaService {

    private final PropostaRepository propostaRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<Proposta> listarTodos() {
        return propostaRepository.findAll();
    }

    public Proposta buscarPorId(Long id) {
        return propostaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proposta não encontrada com id " + id));
    }

    public List<Proposta> listarPorTitulo(String titulo) {
        return propostaRepository.findByTituloContainingIgnoreCase(titulo);
    }
    public List<Proposta> listarPorCategoria(String categoria) {
        return propostaRepository.findByCategoriaIgnoreCase(categoria);
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
        propostaExistente.setCategoria(dadosAtualizados.getCategoria());
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

        Proposta propostaOrigem = buscarPorId(propostaOrigemId);

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
        contraProposta.setCategoria(dto.categoria());
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

    public Proposta enviarParaHomologacao(Long propostaId){

        Proposta proposta = buscarPorId(propostaId);
        proposta.validarPropostaOriginal().enviarParaHomologacao(proposta);
        propostaRepository.save(proposta);
        return proposta;

    }

    public Proposta voltarParaRascunho(Long propostaId){

        Proposta proposta = buscarPorId(propostaId);
        proposta.validarPropostaOriginal().voltarParaRascunho(proposta);
        propostaRepository.save(proposta);
        return proposta;

    }

    public Proposta aceitarHomologacao(Long propostaId){

        Proposta proposta = buscarPorId(propostaId);
        proposta.validarPropostaOriginal().aceitarHomologacao(proposta);
        propostaRepository.save(proposta);
        return proposta;

    }

    public Proposta desativarProposta(Long propostaId){

        Proposta proposta = buscarPorId(propostaId);
        proposta.validarPropostaOriginal().desativarProposta(proposta);
        propostaRepository.save(proposta);
        return proposta;

    }

    @Transactional
    public Proposta aceitarContraProposta(
            Long propostaId,
            Long contraId) {

        Proposta proposta = buscarPorId(propostaId);
        Proposta contraProposta = buscarContraProposta(propostaId, contraId);

        // Aceita a contra-proposta
        contraProposta.validarContraProposta().aceitar(contraProposta);

        // Coloca a proposta original em negociação
        proposta.validarPropostaOriginal().aceitarContraProposta(proposta);

        // Busca todas as contra-propostas da proposta original
        List<Proposta> contraPropostas =
                propostaRepository.findByPropostaOrigemId(propostaId);

        // Recusa todas as outras
        for (Proposta contra : contraPropostas) {

            if (!contra.getId().equals(contraId)) {
                contra.validarPropostaOriginal().cancelar(contra);
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

        Proposta proposta = buscarPorId(propostaId);
        Proposta contraProposta = buscarContraProposta(propostaId, contraId);

        // Finaliza a contra-proposta
        contraProposta.validarContraProposta().finalizar(contraProposta);

        // Finaliza a proposta original
        proposta.validarPropostaOriginal().finalizarNegociacao(proposta);

        propostaRepository.save(proposta);
        propostaRepository.save(contraProposta);

        // Cria a data da conclusão
        LocalDateTime  dataConclusao = LocalDateTime.now();

        //Busca e cancela as contra-propostas em RASCUNHO e EM_ANALISE ligadas a essa proposta
        List<Proposta> contraPropostas =
                propostaRepository.findByPropostaOrigemId(propostaId);

        for (Proposta contra : contraPropostas) {

            if (!contra.getId().equals(contraId)) {
                contra.validarContraProposta().recusar(contra);
//                ou se for deletar --> PropostaRepository.delete(contra);
            }
        }

        // Quem publica — injeta ApplicationEventPublisher
        applicationEventPublisher.publishEvent(new TrocaConcluidaEvent(
                proposta.getId(),
                proposta.getUsuarioProprietario(),
                contraProposta.getUsuarioProprietario(),
                proposta,
                contraProposta,
                dataConclusao));

        return List.of(proposta, contraProposta);
    }

    public Proposta enviarParaAnalise(Long propostaId, Long contraPropostaId){

        Proposta contraProposta = buscarContraProposta(propostaId, contraPropostaId);
        contraProposta.validarContraProposta().enviarParaAnalise(contraProposta);
        propostaRepository.save(contraProposta);
        return contraProposta;

    }

    public Proposta recusarProposta(Long propostaId, Long contraPropostaId){

        Proposta contraProposta = buscarContraProposta(propostaId, contraPropostaId);
        Proposta proposta = buscarPorId(propostaId);
        contraProposta.validarContraProposta().recusar(contraProposta);
        proposta.validarPropostaOriginal().negociacaoFalhou(proposta);

        propostaRepository.save(contraProposta);
        propostaRepository.save(proposta);
        return contraProposta;

    }

    public Proposta cancelarProposta(Long propostaId){

        Proposta proposta = buscarPorId(propostaId);
        proposta.validarPropostaOriginal().cancelar(proposta);
        propostaRepository.save(proposta);
        return proposta;
    }

    public Proposta cancelarContraProposta(Long propostaId, Long contraId){

        Proposta contraProposta = buscarContraProposta(propostaId, contraId);
        contraProposta.validarContraProposta().cancelar(contraProposta);
        propostaRepository.save(contraProposta);
        return contraProposta;
    }




}
