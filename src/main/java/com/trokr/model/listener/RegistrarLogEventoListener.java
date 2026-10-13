package com.trokr.model.listener;

import com.trokr.adapter.LogEventoService;
import com.trokr.model.event.TrocaConcluidaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RegistrarLogEventoListener {

    private final LogEventoService logEventoService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoConcluirTroca(TrocaConcluidaEvent evento) {

        Map<String, Object> payload = Map.of(
                "propostaId", evento.getPropostaA().getId(),
                "contraPropostaId", evento.getPropostaB().getId(),
                "usuarioAId", evento.getUsuarioA().getId(),
                "usuarioBId", evento.getUsuarioB().getId(),
                "tituloProposta", evento.getPropostaA().getTitulo(),
                "tituloContraProposta", evento.getPropostaB().getTitulo(),
                "dataConclusao", evento.getDataConclusao().toString()
        );

        String mensagem = String.format(
                "Troca concluída entre %s e %s: '%s' por '%s'.",
                evento.getUsuarioA().getNome(),
                evento.getUsuarioB().getNome(),
                evento.getPropostaA().getTitulo(),
                evento.getPropostaB().getTitulo()
        );

        logEventoService.registrarInfo(
                "TROCA_CONCLUIDA",
                "PropostaService",
                mensagem,
                payload
        );
    }
}