package com.samuel.sistema.service;

import com.samuel.sistema.dto.CondominioRequest;
import com.samuel.sistema.dto.CondominioResponseDTO;
import com.samuel.sistema.model.Condominio;
import com.samuel.sistema.model.Papel;
import com.samuel.sistema.model.Usuario;
import com.samuel.sistema.repository.CondominioRepository;
import com.samuel.sistema.repository.UsuarioRepository;
import com.samuel.sistema.security.Acesso;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/*
 * Permissões:
 *  Cadastrar / Editar / Excluir ........ somente TECNICO (controller, @PreAuthorize)
 *  Listar / Pesquisar .................. TECNICO: todos | RESPONSAVEL: apenas o próprio
 *  Visualizar .......................... TECNICO: todos | RESPONSAVEL: apenas o próprio
 */
@Service
public class CondominioService {

    private final CondominioRepository repository;
    private final UsuarioRepository usuarioRepository;

    public CondominioService(CondominioRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario buscarResponsavel(Long id) {
        Usuario u = usuarioRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário responsável não encontrado"));
        if (u.getPapel() != Papel.RESPONSAVEL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuário informado não é um responsável");
        }
        return u;
    }

    private Condominio buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Condomínio não encontrado"));
    }

    private void preencher(Condominio c, CondominioRequest req) {
        c.setNome(req.nome());
        c.setEndereco(req.endereco());
        c.setTelefone(req.telefone());
        c.setEmail(req.email());
        c.setResponsavel(buscarResponsavel(req.idResponsavel()));
    }

    public CondominioResponseDTO buscarPorId(Long id, Usuario logado) {
        Condominio c = buscarEntidade(id);
        Acesso.verificar(logado, c);
        return DtoMapper.toCondominio(c);
    }

    /**
     * Busca por termo (nome, endereço, e-mail ou CNPJ). Termo vazio devolve tudo o que o usuário pode ver.
     * A filtragem é feita em memória porque o CNPJ está criptografado (com IV aleatório não dá para usar LIKE no SQL).
     */
    public List<CondominioResponseDTO> pesquisar(String termo, Usuario logado) {
        String t = termo == null ? "" : termo.trim().toLowerCase();

        List<Condominio> base = logado.getPapel() == Papel.TECNICO
                ? repository.findAll()
                : repository.findByResponsavel_IdUsuario(logado.getIdUsuario());

        return base.stream()
                .filter(c -> t.isEmpty()
                        || contem(c.getNome(), t)
                        || contem(c.getEndereco(), t)
                        || contem(c.getEmail(), t))
                .sorted(Comparator.comparing(Condominio::getNome, String.CASE_INSENSITIVE_ORDER))
                .map(DtoMapper::toCondominio)
                .toList();
    }

    private boolean contem(String campo, String termo) {
        return campo != null && campo.toLowerCase().contains(termo);
    }

    public CondominioResponseDTO cadastrarCondominio(CondominioRequest req) {
        Condominio c = new Condominio();
        preencher(c, req);
        c.setCreated_at(LocalDateTime.now());
        return DtoMapper.toCondominio(repository.save(c));
    }

    public CondominioResponseDTO editarCondominio(Long id, CondominioRequest req) {
        Condominio c = buscarEntidade(id);
        preencher(c, req);
        c.setUpdated_at(LocalDateTime.now());
        return DtoMapper.toCondominio(repository.save(c));
    }

    public void excluirCondominio(Long id) {
        // se tiver defeitos vinculados, o banco recusa e o GlobalExceptionHandler devolve 409
        repository.delete(buscarEntidade(id));
    }
}
