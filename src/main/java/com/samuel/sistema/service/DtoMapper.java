package com.samuel.sistema.service;

import com.samuel.sistema.dto.ChamadoResponseDTO;
import com.samuel.sistema.dto.CondominioResponseDTO;
import com.samuel.sistema.dto.DefeitoResponseDTO;
import com.samuel.sistema.model.Chamado;
import com.samuel.sistema.model.Condominio;
import com.samuel.sistema.model.Defeito;

public final class DtoMapper {

    private DtoMapper() {
    }

    public static CondominioResponseDTO toCondominio(Condominio c) {
        return new CondominioResponseDTO(
                c.getIdCondominio(),
                c.getNome(),
                c.getEndereco(),
                c.getTelefone(),
                c.getEmail(),
                c.getResponsavel() != null ? c.getResponsavel().getIdUsuario() : null,
                c.getResponsavel() != null ? c.getResponsavel().getNome() : null
        );
    }

    public static DefeitoResponseDTO toDefeito(Defeito d) {
        return new DefeitoResponseDTO(
                d.getIdDefeito(),
                d.getTitulo(),
                d.getDescricao(),
                d.getLocal(),
                d.getCondominio().getIdCondominio(),
                d.getCondominio().getNome()
        );
    }

    public static ChamadoResponseDTO toChamado(Chamado ch) {
        return new ChamadoResponseDTO(
                ch.getIdChamado(),
                ch.getTipo(),
                ch.getDescricao(),
                ch.getDataAgendamento(),
                ch.getStatus(),
                ch.getObservacoes(),
                ch.getUsuario() != null ? ch.getUsuario().getNome() : null,
                toDefeito(ch.getDefeito()),
                toCondominio(ch.getDefeito().getCondominio())
        );
    }
}
