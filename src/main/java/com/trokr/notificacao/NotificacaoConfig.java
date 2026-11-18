package com.trokr.notificacao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class NotificacaoConfig {

    @Bean
    @Primary
    public NotificacaoPort notificacaoPort(
            NotificacaoSimuladaAdapter adaptador,
            NotificacaoProperties props) {

        return new NotificacaoComRetry(
                    adaptador,
                props.getMaxTentativas(),
                props.getEsperaInicialMs()
        );
    }
}