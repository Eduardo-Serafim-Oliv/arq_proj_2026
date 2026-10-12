package com.trokr.adpter;

import org.springframework.stereotype.Component;

@Component
public class RegistroDeEventosMongo implements RegistroDeEventos{

//    private final LogEventoRepository mongoRepository;

    public void registrar(String tipo, String mensagem, Object payload) {
//        LogEvento doc = new LogEvento(tipo, mensagem, payload);
//        mongoRepository.insert(doc);
    }

//    buscarPorTipo() — tradução parecida, na direção inversa

}
