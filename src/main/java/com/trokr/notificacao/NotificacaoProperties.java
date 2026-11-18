package com.trokr.notificacao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "trokr.notificacao")
public class NotificacaoProperties {

    // Configurações do retry
    private int maxTentativas = 3;
    private long esperaInicialMs = 200;

    // Configurações da simulação
    private long latenciaMs = 500;
    private double taxaFalha = 0.5;
}