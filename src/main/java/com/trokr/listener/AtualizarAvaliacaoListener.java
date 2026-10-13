package com.trokr.listener;

import com.trokr.model.Avaliacao;
import com.trokr.model.StatusAvaliacao;
import com.trokr.event.TrocaConcluidaEvent;
import com.trokr.repository.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AtualizarAvaliacaoListener {

    private final AvaliacaoRepository avaliacaoRepository;

    @EventListener
    public void aoConcluirTroca(TrocaConcluidaEvent evento) {

        // Usuário A avalia Usuário B
        Avaliacao avaliacao1 = new Avaliacao();
        avaliacao1.setUsuarioAvaliador(evento.getUsuarioA());
        avaliacao1.setUsuarioAvaliado(evento.getUsuarioB());
        avaliacao1.setStatus(StatusAvaliacao.PENDENTE);

        // Usuário B avalia Usuário A
        Avaliacao avaliacao2 = new Avaliacao();
        avaliacao2.setUsuarioAvaliador(evento.getUsuarioB());
        avaliacao2.setUsuarioAvaliado(evento.getUsuarioA());
        avaliacao2.setStatus(StatusAvaliacao.PENDENTE);

        avaliacaoRepository.save(avaliacao1);
        avaliacaoRepository.save(avaliacao2);

    }
}
