package com.samuel.sistema.service;

import com.samuel.sistema.dto.LoginRequest;
import com.samuel.sistema.dto.LoginResponse;
import com.samuel.sistema.dto.UsuarioLogadoResponse;
import com.samuel.sistema.model.Usuario;
import com.samuel.sistema.repository.UsuarioRepository;
import com.samuel.sistema.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        // mesma mensagem para e-mail inexistente e senha errada (não revela quais e-mails existem)
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .filter(u -> passwordEncoder.matches(request.senha(), u.getSenhaHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos"));

        return new LoginResponse(
                jwtService.gerarToken(usuario),
                usuario.getNome(),
                usuario.getPapel().name(),
                jwtService.getExpiracaoMinutos()
        );
    }

    public UsuarioLogadoResponse dadosDoUsuario(Usuario usuario) {
        return new UsuarioLogadoResponse(
                usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getPapel().name());
    }
}
