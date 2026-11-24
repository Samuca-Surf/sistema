package com.samuel.sistema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ChamadoRequest(
        @NotBlank(message = "Tipo é obrigatório (Preventiva ou Corretiva)") String tipo,
        @NotBlank(message = "Descrição é obrigatória") String descricao,
        @NotNull(message = "Data de agendamento é obrigatória") LocalDateTime dataAgendamento,
        String status, // opcional: se vier vazio, assume "Pendente"
        String observacoes,
        @NotNull(message = "Defeito é obrigatório") Long idDefeito
) {
}
