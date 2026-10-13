package com.trokr.service;

import com.trokr.model.CategoriaProposta;
import com.trokr.model.Proposta;
import com.trokr.strategy.EstrategiaCredito;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalculadoraCreditoService {

    private final Map<CategoriaProposta, EstrategiaCredito> estrategias;
    public CalculadoraCreditoService(List<EstrategiaCredito> todas) {
        this.estrategias = todas.stream()
                .collect(Collectors.toMap(EstrategiaCredito::categoria, e -> e));
    }
    public int calcular(Proposta proposta) {
        EstrategiaCredito estrategia = estrategias.get(proposta.getCategoria());
        if (estrategia == null) {
            throw new IllegalStateException("Sem estratégia para " + proposta.getCategoria());
        }
        return estrategia.calcular(proposta);
    }
}
