package com.trokr.notificacao;
// Porta de saída: o que o domínio precisa
// para avisar um usuário. Pode lançar
// NotificacaoIndisponivelException.

public interface NotificacaoPort {
    void notificar(Long usuarioId, String mensagem);
}