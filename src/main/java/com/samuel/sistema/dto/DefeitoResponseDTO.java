package com.samuel.sistema.dto;

public record DefeitoResponseDTO(
        Long idDefeito,
        String titulo,
        String descricao,
        String local,
        Long idCondominio,
        String nomeCondominio
) {
}
