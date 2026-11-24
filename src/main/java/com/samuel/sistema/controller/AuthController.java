package com.samuel.sistema.controller;

import com.samuel.sistema.dto.LoginRequest;
import com.samuel.sistema.dto.LoginResponse;
import com.samuel.sistema.dto.UsuarioLogadoResponse;
import com.samuel.sistema.model.Usuario;
import com.samuel.sistema.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }

    /** Usado pela tela principal para mostrar o nome do usuário logado. */
    @GetMapping("/me")
    public UsuarioLogadoResponse me(@AuthenticationPrincipal Usuario usuario) {
        return service.dadosDoUsuario(usuario);
    }
}
