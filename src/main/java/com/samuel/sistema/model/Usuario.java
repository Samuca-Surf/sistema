package com.samuel.sistema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nome;
    private String email;
    private String senhaHash;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @Enumerated(EnumType.STRING)
    private Papel pepel; //responsavel pelo condominio ou o tecnico

    public Usuario() {

    }

    public Papel getPapel() {
        return this.pepel;
    }
}
