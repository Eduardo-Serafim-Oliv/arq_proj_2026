package com.trokr.service;

import com.trokr.exception.RecursoNaoEncontradoException;
import com.trokr.model.Avaliacao;
import com.trokr.repository.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    public Avaliacao buscarPorId(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("AVALIACAO_NAO_ENCONTRADA","Avaliação não encontrada com o id: " + id)
                );
    }

    public Avaliacao salvar(Avaliacao avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }
}