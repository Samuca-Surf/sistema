package com.samuel.sistema.service;

import com.samuel.sistema.dto.ChamadoRequest;
import com.samuel.sistema.dto.ChamadoResponseDTO;
import com.samuel.sistema.model.Chamado;
import com.samuel.sistema.model.Defeito;
import com.samuel.sistema.model.Papel;
import com.samuel.sistema.model.Usuario;
import com.samuel.sistema.repository.ChamadoRepository;
import com.samuel.sistema.repository.DefeitoRepository;
import com.samuel.sistema.security.Acesso;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ChamadoService {

    private static final Set<String> TIPOS = Set.of("Preventiva", "Corretiva");
    private static final Set<String> STATUS = Set.of("Pendente", "Em andamento", "Concluído");

    private final ChamadoRepository repository;
    private final DefeitoRepository defeitoRepository;

    public ChamadoService(ChamadoRepository repository, DefeitoRepository defeitoRepository) {
        this.repository = repository;
        this.defeitoRepository = defeitoRepository;
    }

    private Defeito buscarDefeito(Long id) {
        return defeitoRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Defeito não encontrado"));
    }

    private Chamado buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Chamado não encontrado"));
    }

    private String validarTipo(String tipo) {
        if (!TIPOS.contains(tipo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo deve ser: Preventiva ou Corretiva");
        }
        return tipo;
    }

    private String validarStatus(String status) {
        if (status == null || status.isBlank()) return "Pendente";
        if (!STATUS.contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Status deve ser: Pendente, Em andamento ou Concluído");
        }
        return status;
    }

    /** Listagem ordenada pelo dia de agendamento, com dados do condomínio e do defeito. */
    public List<ChamadoResponseDTO> listar(Usuario logado) {
        List<Chamado> lista = logado.getPapel() == Papel.TECNICO
                ? repository.findAllByOrderByDataAgendamentoAsc()
                : repository.findByDefeito_Condominio_Responsavel_IdUsuarioOrderByDataAgendamentoAsc(
                        logado.getIdUsuario());
        return lista.stream().map(DtoMapper::toChamado).toList();
    }

    public ChamadoResponseDTO cadastrar(ChamadoRequest req, Usuario logado) {
        Defeito defeito = buscarDefeito(req.idDefeito());
        Acesso.verificar(logado, defeito.getCondominio());

        Chamado c = new Chamado();
        c.setTipo(validarTipo(req.tipo()));
        c.setDescricao(req.descricao());
        c.setDataAgendamento(req.dataAgendamento());
        c.setStatus(validarStatus(req.status()));
        c.setObservacoes(req.observacoes());
        c.setDefeito(defeito);
        c.setUsuario(logado);
        c.setCreatedAt(LocalDateTime.now());
        return DtoMapper.toChamado(repository.save(c));
    }

    public ChamadoResponseDTO editar(Long id, ChamadoRequest req) {
        Chamado c = buscarEntidade(id);
        c.setTipo(validarTipo(req.tipo()));
        c.setDescricao(req.descricao());
        c.setDataAgendamento(req.dataAgendamento());
        c.setStatus(validarStatus(req.status()));
        c.setObservacoes(req.observacoes());
        c.setDefeito(buscarDefeito(req.idDefeito()));
        return DtoMapper.toChamado(repository.save(c));
    }

    public void excluir(Long id) {
        repository.delete(buscarEntidade(id));
    }
}
