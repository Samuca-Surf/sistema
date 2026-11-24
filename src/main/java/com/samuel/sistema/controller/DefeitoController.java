package com.samuel.sistema.controller;

import com.samuel.sistema.dto.DefeitoRequestDTO;
import com.samuel.sistema.dto.DefeitoResponseDTO;
import com.samuel.sistema.model.Usuario;
import com.samuel.sistema.service.DefeitoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/defeitos")
public class DefeitoController {

    private final DefeitoService service;

    public DefeitoController(DefeitoService service) {
        this.service = service;
    }

    @GetMapping
    public List<DefeitoResponseDTO> listar(@RequestParam(required = false) Long idCondominio,
                                           @AuthenticationPrincipal Usuario logado) {
        return service.listar(idCondominio, logado);
    }

    @PostMapping
    public ResponseEntity<DefeitoResponseDTO> cadastrar(@Valid @RequestBody DefeitoRequestDTO request,
                                                        @AuthenticationPrincipal Usuario logado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(request, logado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TECNICO')")
    public DefeitoResponseDTO editar(@PathVariable Long id, @Valid @RequestBody DefeitoRequestDTO request) {
        return service.editar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
