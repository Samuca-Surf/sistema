package com.samuel.sistema.dto;

public record LoginResponse(
        String token,
        String nome,
        String papel,
        long expiraEmMinutos
) {
}
