package com.trokr.model;

public enum StatusProposta {

    // Estados de Proposta

    RASCUNHO("Rascunho"),
    HOMOLOGACAO("Homologação"),
    ATIVO("Ativo"),
    NEGOCIADO("Negociado"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado"),

   //  Estados de Contra-Proposta

    EM_ANALISE("Em análise"),
    NEGOCIADO_CONTRA("Negociado contra"),
    FINALIZADO_CONTRA("Finalizado contra"),
    RECUSADO("Recusado"),
    CANCELADO_CONTRA("Cancelado contra");

    // Descrição amigável dos Estados

    private final String descricao;

    StatusProposta(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}