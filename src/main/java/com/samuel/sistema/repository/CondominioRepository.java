package com.samuel.sistema.repository;

import com.samuel.sistema.model.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CondominioRepository extends JpaRepository<Condominio, Long> {
    List<Condominio> findByResponsavel_IdUsuario(Long idUsuario);
}
