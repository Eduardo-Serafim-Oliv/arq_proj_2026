package com.trokr.adpter;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LogEventoService {

//    private final LogEventoRepository logEventoRepository;

    public void registrarInfo(String origem, String mensagem, Map<String, Object> payload) {
        // TODO: montar LogEvento com nivel INFO e salvar
    }
    public void registrarErro(String origem, String mensagem, Exception excecao) {
        // TODO: montar LogEvento com nivel ERROR, payload contendo a mensagem da exceção, e salvar
    }
}