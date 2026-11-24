package com.samuel.sistema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Defeito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDefeito;

    private String titulo;
    private String descricao;
    private String local; //local onde esta o defeito
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;

    @OneToMany(mappedBy = "defeito")
    private List<Chamado> chamado;

    public Defeito(){

    }
}
