package com.samuel.sistema.dto;

public record DefeitoRequestDTO (
    String titulo,
    String descricao,
    String local,
    Long idCondominio
){}