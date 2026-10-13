package com.trokr.strategy;

import com.trokr.model.CategoriaProposta;
import com.trokr.model.Proposta;

public interface EstrategiaCredito {

    CategoriaProposta categoria();
    int calcular(Proposta proposta);
}
