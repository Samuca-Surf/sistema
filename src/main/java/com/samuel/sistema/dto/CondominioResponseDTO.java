package com.samuel.sistema.dto;

public record CondominioResponseDTO(
        Long idCondominio,
        String nome,
        String endereco,
        String telefone,
        String email,
        Long idResponsavel,
        String nomeResponsavel
) {
}
