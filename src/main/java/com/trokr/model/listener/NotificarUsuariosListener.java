package com.trokr.model.listener;

import com.trokr.model.event.TrocaConcluidaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificarUsuariosListener {

    @EventListener
    public void aoConcluirTroca(TrocaConcluidaEvent evento) {

        String mensagem = String.format(
                "Troca concluída! Usuário %s trocou '%s' com Usuário %s, "
                        + "que ofereceu '%s'. Data da conclusão: %s",
                evento.getUsuarioA().getNome(),
                evento.getPropostaA().getTitulo(),
                evento.getUsuarioB().getNome(),
                evento.getPropostaB().getTitulo(),
                evento.getDataConclusao()
        );

        System.out.println(mensagem);
//        log.info(mensagem);

    }
}