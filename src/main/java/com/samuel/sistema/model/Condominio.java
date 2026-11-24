package com.samuel.sistema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Condominio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCondominio;

    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @OneToMany
    private List<Defeito> defeitos;
    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    public Condominio(){

    }
}
