package com.trokr.model.listener;

import com.trokr.model.Historico;
import com.trokr.model.StatusProposta;
import com.trokr.model.event.TrocaConcluidaEvent;
import com.trokr.repository.HistoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrarHistoricoListener {

    private final HistoricoRepository historicoRepository;

    @EventListener
    public void aoConcluirTroca(TrocaConcluidaEvent evento) {

        // Histórico da proposta original
        Historico historicoProposta = new Historico();

        historicoProposta.setProposta(evento.getPropostaA());
        historicoProposta.setStatusAnterior(StatusProposta.NEGOCIADO);
        historicoProposta.setStatusNovo(StatusProposta.FINALIZADO);
        historicoProposta.setData(evento.getDataConclusao());

        // Histórico da contra-proposta
        Historico historicoContraProposta = new Historico();

        historicoContraProposta.setProposta(evento.getPropostaB());
        historicoContraProposta.setStatusAnterior(StatusProposta.NEGOCIADO_CONTRA);
        historicoContraProposta.setStatusNovo(StatusProposta.FINALIZADO_CONTRA);
        historicoContraProposta.setData(evento.getDataConclusao());

        historicoRepository.save(historicoProposta);
        historicoRepository.save(historicoContraProposta);
    }
}