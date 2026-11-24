package com.samuel.sistema.dto;

public record CondominioRequest(
        String nome,
        String endereco,
        String telefone,
        String email,
        Long idResponsavel
) {
}
