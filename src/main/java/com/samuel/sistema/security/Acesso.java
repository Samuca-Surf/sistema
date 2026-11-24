package com.samuel.sistema.security;

import com.samuel.sistema.model.Condominio;
import com.samuel.sistema.model.Papel;
import com.samuel.sistema.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Regra de acesso: técnico vê tudo; responsável só vê o próprio condomínio. */
public final class Acesso {

    private Acesso() {
    }

    public static void verificar(Usuario logado, Condominio condominio) {
        if (logado.getPapel() == Papel.TECNICO) return;

        boolean dono = condominio.getResponsavel() != null
                && condominio.getResponsavel().getIdUsuario().equals(logado.getIdUsuario());
        if (!dono) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem acesso a este condomínio");
        }
    }
}
