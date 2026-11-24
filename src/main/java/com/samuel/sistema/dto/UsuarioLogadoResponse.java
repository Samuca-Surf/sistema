package com.samuel.sistema.dto;

public record UsuarioLogadoResponse(
        Long idUsuario,
        String nome,
        String email,
        String papel
) {
}
