package com.trokr.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trokr.adapter.LogEvento;
import com.trokr.adapter.NivelLog;
import com.trokr.adapter.RegistroDeEventos;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogEventoService {

    private final RegistroDeEventos registroDeEventos;

    public void registrarInfo(String tipo, String origem, String mensagem, Map<String, Object> payload) {
        registroDeEventos.registrar(tipo, NivelLog.INFO, origem, mensagem, payload);
    }

    public void registrarErro(String tipo, String origem, String mensagem, Exception excecao) {

        Map<String, Object> payload = new HashMap<>();

        payload.put("tipoExcecao", excecao.getClass().getName());
        payload.put("mensagemExcecao", String.valueOf(excecao.getMessage()));

        registroDeEventos.registrar(tipo, NivelLog.ERROR, origem, mensagem, payload);
    }

    public List<LogEvento> listarPorTipo(String tipo) {
        return registroDeEventos.buscarPorTipo(tipo);
    }

    public List<LogEvento> listarPorNivelEPeriodo(NivelLog nivel, LocalDateTime inicio, LocalDateTime fim) {
        return registroDeEventos.buscarPorNivelEPeriodo(nivel, inicio, fim);
    }
}