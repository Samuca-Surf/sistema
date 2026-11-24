package com.samuel.sistema.repository;

import com.samuel.sistema.model.Defeito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefeitoRepository extends JpaRepository<Defeito, Long> {
    List<Defeito> findByCondominio_IdCondominio(Long idCondominio);

    List<Defeito> findByCondominio_Responsavel_IdUsuario(Long idUsuario);
}
