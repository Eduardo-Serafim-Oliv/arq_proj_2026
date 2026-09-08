package com.trokr.model.state;

import com.trokr.model.Proposta;
import com.trokr.model.StatusProposta;

public interface EstadoProposta {

        void enviarParaHomologacao(Proposta proposta); //Avança para homologação
        void voltarParaRascunho(Proposta proposta); // Volta para rascunho
        void aceitarHomologacao(Proposta proposta); // Avança para ativar
        void desativarProposta(Proposta proposta);  // Volta para rascunho
        void aceitarContraProposta(Proposta proposta);  // Avança para negociação
        void negociacaoFalhou(Proposta proposta);  // Volta para ativa
        void finalizarNegociacao(Proposta proposta); // Avança para finalizado
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