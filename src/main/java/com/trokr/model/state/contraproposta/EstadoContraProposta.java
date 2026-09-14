package com.trokr.model.state.contraproposta;

import com.trokr.model.Proposta;
import com.trokr.model.StatusProposta;

public interface EstadoContraProposta {

        void enviarParaAnalise(Proposta proposta); //Avança para Em Analise
        void aceitar(Proposta proposta); // Avança para negociado contra
        void recusar(Proposta proposta);  // Avança para recusado
        void finalizar(Proposta proposta);  // Avança para finalizado
        void cancelar(Proposta proposta); // Avança para cancelado
        StatusProposta getStatus();
}

//avancar
//enviarParaHomologação
//aprovar
//recusarHomologação
//revisar
//cancelar
//aceitarContraProposta
//negociacaoFalhou