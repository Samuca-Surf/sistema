package com.samuel.sistema.dto;

import java.time.LocalDateTime;

public record ChamadoResponseDTO(
        Long idChamado,
        String tipo,
        String descricao,
        LocalDateTime dataAgendamento,
        String status,
        String observacoes,
        String cadastradoPor,
        DefeitoResponseDTO defeito,
        CondominioResponseDTO condominio
) {
}
