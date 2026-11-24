package com.samuel.sistema.service;

import com.samuel.sistema.dto.DefeitoRequestDTO;
import com.samuel.sistema.dto.DefeitoResponseDTO;
import com.samuel.sistema.model.Condominio;
import com.samuel.sistema.model.Defeito;
import com.samuel.sistema.model.Papel;
import com.samuel.sistema.model.Usuario;
import com.samuel.sistema.repository.CondominioRepository;
import com.samuel.sistema.repository.DefeitoRepository;
import com.samuel.sistema.security.Acesso;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefeitoService {

    private final DefeitoRepository repository;
    private final CondominioRepository condominioRepository;

    public DefeitoService(DefeitoRepository repository, CondominioRepository condominioRepository) {
        this.repository = repository;
        this.condominioRepository = condominioRepository;
    }

    private Condominio buscarCondominio(Long id) {
        return condominioRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Condomínio não encontrado"));
    }

    private Defeito buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Defeito não encontrado"));
    }

    public List<DefeitoResponseDTO> listar(Long idCondominio, Usuario logado) {
        List<Defeito> lista;
        if (logado.getPapel() == Papel.TECNICO) {
            lista = idCondominio == null ? repository.findAll()
                    : repository.findByCondominio_IdCondominio(idCondominio);
        } else {
            lista = repository.findByCondominio_Responsavel_IdUsuario(logado.getIdUsuario()).stream()
                    .filter(d -> idCondominio == null || d.getCondominio().getIdCondominio().equals(idCondominio))
                    .toList();
        }
        return lista.stream().map(DtoMapper::toDefeito).toList();
    }

    public DefeitoResponseDTO cadastrar(DefeitoRequestDTO req, Usuario logado) {
        Condominio condominio = buscarCondominio(req.idCondominio()); // todo defeito pertence a um condomínio
        Acesso.verificar(logado, condominio);

        Defeito d = new Defeito();
        d.setTitulo(req.titulo());
        d.setDescricao(req.descricao());
        d.setLocal(req.local());
        d.setCondominio(condominio);
        d.setCreated_at(LocalDateTime.now());
        return DtoMapper.toDefeito(repository.save(d));
    }

    public DefeitoResponseDTO editar(Long id, DefeitoRequestDTO req) {
        Defeito d = buscarEntidade(id);
        d.setTitulo(req.titulo());
        d.setDescricao(req.descricao());
        d.setLocal(req.local());
        d.setCondominio(buscarCondominio(req.idCondominio()));
        return DtoMapper.toDefeito(repository.save(d));
    }

    public void excluir(Long id) {
        repository.delete(buscarEntidade(id));
    }
}
