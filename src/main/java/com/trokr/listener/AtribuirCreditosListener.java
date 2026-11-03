package com.trokr.listener;

import com.trokr.config.AtrasoDemo;
import com.trokr.model.Usuario;
import com.trokr.event.TrocaConcluidaEvent;
import com.trokr.repository.UsuarioRepository;
import com.trokr.service.CalculadoraCreditoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AtribuirCreditosListener {

    private final CalculadoraCreditoService calculadora;
    private final UsuarioRepository usuarioRepository;
    private final AtrasoDemo atrasoDemontracao;


    @EventListener
    @Transactional
    public void aoConcluirTroca(TrocaConcluidaEvent evento) {

        // Busca os usuários no banco
        Usuario usuarioA = usuarioRepository
                .findById(evento.getUsuarioA().getId())
                .orElseThrow(() ->
                        new IllegalStateException("Usuário A não encontrado.")
                );

        Usuario usuarioB = usuarioRepository
                .findById(evento.getUsuarioB().getId())
                .orElseThrow(() ->
                        new IllegalStateException("Usuário B não encontrado.")
                );

        // Usuário A recebe créditos pela proposta que ofereceu
        int creditosA = calculadora.calcular(evento.getPropostaA());

        // Usuário B recebe créditos pela contra-proposta que ofereceu
        int creditosB = calculadora.calcular(evento.getPropostaB());

        // Ponto onde a janela deve abrir para que ocorra conconrrência no saldo:
        // Para usar, mude o atraso no application.yml
        atrasoDemontracao.aplicar();

        // Atualiza os saldos
        // Agora com UPDATE atômico, utilizando uma query direta no repository:

        usuarioRepository.adicionarCreditos(usuarioA.getId(), creditosA);
        usuarioRepository.adicionarCreditos(usuarioB.getId(), creditosB);

    }
}