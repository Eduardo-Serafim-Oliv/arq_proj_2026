package com.trokr.strategy.credits;

import com.trokr.model.CategoriaProposta;
import com.trokr.model.Proposta;
import com.trokr.strategy.EstrategiaCredito;
import org.springframework.stereotype.Component;

@Component
public class CreditoProduto implements EstrategiaCredito {

    @Override
    public CategoriaProposta categoria() {
        return CategoriaProposta.PRODUTO;
    }

    @Override
    public int calcular(Proposta proposta) {
        return 10;
    }
}