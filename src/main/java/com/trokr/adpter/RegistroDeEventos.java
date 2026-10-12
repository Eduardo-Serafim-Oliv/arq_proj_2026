package com.trokr.adpter;

import java.util.List;

public interface RegistroDeEventos {

    void registrar(String tipo, String mensagem, Object payload);
//    List<Object> buscarPorTipo(String tipo);
}
