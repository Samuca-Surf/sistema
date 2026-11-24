package com.samuel.sistema.repository;

import com.samuel.sistema.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    List<Chamado> findAllByOrderByDataAgendamentoAsc();

    List<Chamado> findByDefeito_Condominio_Responsavel_IdUsuarioOrderByDataAgendamentoAsc(Long idUsuario);
}
