package com.trokr.service;

import com.trokr.exception.ResourceNotFoundException;
import com.trokr.model.Proposta;
import com.trokr.model.Usuario;
import com.trokr.repository.PropostaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropostaService {

    private final PropostaRepository propostaRepository;
    private final UsuarioService usuarioService;

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

}
