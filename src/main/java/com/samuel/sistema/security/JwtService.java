package com.samuel.sistema.security;

import com.samuel.sistema.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey chave;
    private final long expiracaoMinutos;

    public JwtService(@Value("${jwt.secret}") String segredo,
                      @Value("${jwt.expiration-minutes}") long expiracaoMinutos) {
        this.chave = Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8)); // mínimo 32 caracteres
        this.expiracaoMinutos = expiracaoMinutos;
    }

    public String gerarToken(Usuario usuario) {
        Date agora = new Date();
        Date expira = new Date(agora.getTime() + expiracaoMinutos * 60_000);
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("papel", usuario.getPapel().name())
                .issuedAt(agora)
                .expiration(expira)
                .signWith(chave)
                .compact();
    }

    /** Lança JwtException se o token for inválido, adulterado ou estiver expirado. */
    public String extrairEmail(String token) {
        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public long getExpiracaoMinutos() {
        return expiracaoMinutos;
    }
}
