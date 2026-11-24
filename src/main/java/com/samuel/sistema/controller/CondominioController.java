package com.samuel.sistema.controller;

import com.samuel.sistema.dto.CondominioRequest;
import com.samuel.sistema.dto.CondominioResponseDTO;
import com.samuel.sistema.model.Usuario;
import com.samuel.sistema.service.CondominioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/condominios")
public class CondominioController {

    private final CondominioService service;

    public CondominioController(CondominioService service) {
        this.service = service;
    }

    // GET /condominios            -> lista tudo
    // GET /condominios?busca=bloco -> lista só os que combinam com o termo
    @GetMapping
    public List<CondominioResponseDTO> pesquisar(@RequestParam(required = false) String busca,
                                                 @AuthenticationPrincipal Usuario logado) {
        return service.pesquisar(busca, logado);
    }

    @GetMapping("/{id}")
    public CondominioResponseDTO buscarPorId(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        return service.buscarPorId(id, logado);
    }

    @PostMapping
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<CondominioResponseDTO> cadastrar(@Valid @RequestBody CondominioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarCondominio(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TECNICO')")
    public CondominioResponseDTO editar(@PathVariable Long id, @Valid @RequestBody CondominioRequest request) {
        return service.editarCondominio(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirCondominio(id);
        return ResponseEntity.noContent().build();
    }
}
