package com.samuel.sistema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Chamado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChamado;

    private String tipo; // Preventiva ou Corretiva
    private String descricao;
    private LocalDateTime dataAgendamento;
    private String status; // Pendente, Em andamento ou Concluído
    private String observacoes;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "usuario_id") // usuário que cadastrou o chamado
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "defeito_id", nullable = false)
    private Defeito defeito;

    public Chamado() {
    }
}
