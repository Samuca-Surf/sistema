package com.samuel.sistema.controller;

import com.samuel.sistema.dto.ChamadoRequest;
import com.samuel.sistema.dto.ChamadoResponseDTO;
import com.samuel.sistema.model.Usuario;
import com.samuel.sistema.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ChamadoResponseDTO> listar(@AuthenticationPrincipal Usuario logado) {
        return service.listar(logado);
    }

    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> cadastrar(@Valid @RequestBody ChamadoRequest request,
                                                        @AuthenticationPrincipal Usuario logado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(request, logado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TECNICO')")
    public ChamadoResponseDTO editar(@PathVariable Long id, @Valid @RequestBody ChamadoRequest request) {
        return service.editar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
