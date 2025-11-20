package sistemaprojetos.api_controle_projetos.grupos.model.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistemaprojetos.api_controle_projetos.grupos.client.PessoasClient;
import sistemaprojetos.api_controle_projetos.grupos.client.ProjetosClient;
import sistemaprojetos.api_controle_projetos.grupos.dto.GrupoDTO;
import sistemaprojetos.api_controle_projetos.grupos.dto.GrupoResponseDTO;
import sistemaprojetos.api_controle_projetos.grupos.model.entity.Grupo;
import sistemaprojetos.api_controle_projetos.grupos.model.repository.GrupoRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private PessoasClient pessoasClient;

    @Autowired
    private ProjetosClient projetosClient;

    public GrupoResponseDTO create(GrupoDTO dto) {

        if (grupoRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new IllegalArgumentException("Já existe grupo com este nome.");
        }

        if (dto.coordenadorId() != null) {
            Boolean existe = pessoasClient.professorExiste(dto.coordenadorId());
            if (Boolean.FALSE.equals(existe) || existe == null) {
                throw new EntityNotFoundException("Coordenador com ID " + dto.coordenadorId() + " não encontrado no serviço de pessoas.");
            }
        }

        Grupo grupo = new Grupo(dto);

        Grupo grupoSalvo = grupoRepository.save(grupo);
        return GrupoResponseDTO.fromEntity(grupoSalvo);

    }

    public GrupoResponseDTO update(Long grupoId, GrupoDTO dto) {
        Grupo g = grupoRepository.findById(grupoId).orElseThrow(() -> new EntityNotFoundException("Grupo com ID " + grupoId + " não encontrado."));
        if (dto.nome() != null && !dto.nome().isBlank()
                && !dto.nome().equalsIgnoreCase(g.getNome())) {
            if (grupoRepository.existsByNomeIgnoreCase(dto.nome())) {
                throw new IllegalArgumentException("Já existe grupo com este nome.");
            }

            g.setNome(dto.nome());
        }

        if (dto.descricao() != null) {
            g.setDescricao(dto.descricao());
        }

        if (dto.coordenadorId() != null) {
            Boolean existe = pessoasClient.professorExiste(dto.coordenadorId());
            if (Boolean.FALSE.equals(existe) || existe == null) {
                throw new EntityNotFoundException("Coordenador com ID " + dto.coordenadorId() + " não encontrado no serviço de pessoas.");
            }
               g.setCoordenadorId(dto.coordenadorId());
        }

        Grupo grupoSalvo = grupoRepository.save(g);
        return GrupoResponseDTO.fromEntity(grupoSalvo);
    }


    public GrupoResponseDTO definirDisponibilidade(Long grupoId, boolean disponivel) {
        Grupo g = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new EntityNotFoundException("Grupo com ID " + grupoId + " não encontrado."));
        g.setDisponibilidade(disponivel);
        Grupo grupoSalvo = grupoRepository.save(g);
        return GrupoResponseDTO.fromEntity(grupoSalvo);
    }


    public GrupoResponseDTO substituirAlunos(Long grupoId, List<Long> alunosIds) {
        Grupo g = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new EntityNotFoundException("Grupo com ID " + grupoId + " não encontrado."));

        if (alunosIds == null) {
            alunosIds = Collections.emptyList();
        }

        List<Long> faltantes = pessoasClient.buscarIdsAlunosInexistentes(alunosIds);
        if (faltantes != null && !faltantes.isEmpty()) {
            throw new EntityNotFoundException("Alunos não encontrados no serviço de pessoas: " + faltantes);
        }

        g.getAlunosIds().clear();
        g.getAlunosIds().addAll(alunosIds);

        Grupo grupoSalvo = grupoRepository.save(g);
        return GrupoResponseDTO.fromEntity(grupoSalvo);
    }


    public GrupoResponseDTO adicionarAlunos(Long grupoId, List<Long> alunosIds) {
        Grupo g = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new EntityNotFoundException("Grupo com ID " + grupoId + " não encontrado."));
        if (alunosIds == null || alunosIds.isEmpty())
            return GrupoResponseDTO.fromEntity(g);

        List<Long> faltantes = pessoasClient.buscarIdsAlunosInexistentes(alunosIds);
        if (faltantes != null && !faltantes.isEmpty()) {
            throw new EntityNotFoundException("Alunos não encontrados no serviço de pessoas: " + faltantes);
        }

        List<Long> atuais = g.getAlunosIds();
        if (atuais == null) {
            atuais = new ArrayList<>();
            g.setAlunosIds(atuais);
        }

        for (Long id : alunosIds) {
            if (!atuais.contains(id)) {
                atuais.add(id);
            }
        }

        Grupo grupoSalvo = grupoRepository.save(g);
        return GrupoResponseDTO.fromEntity(grupoSalvo);
    }


    public GrupoResponseDTO removerAlunos(Long grupoId, List<Long> alunosIds) {
        Grupo g = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new EntityNotFoundException("Grupo com ID " + grupoId + " não encontrado."));


        if (alunosIds == null || alunosIds.isEmpty())
            return GrupoResponseDTO.fromEntity(g);


        g.getAlunosIds().removeIf(alunosIds::contains);


        Grupo grupoSalvo = grupoRepository.save(g);
        return GrupoResponseDTO.fromEntity(grupoSalvo);
    }


    public List<GrupoResponseDTO> listarDisponiveis() {
        return grupoRepository.findByDisponibilidadeTrue()
                .stream()
                .map(GrupoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }


    public GrupoResponseDTO findById(Long id) {
        Grupo grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grupo com ID " + id + " não encontrado."));
        return GrupoResponseDTO.fromEntity(grupo);
    }


    public void delete(Long grupoId) {
        Grupo g = grupoRepository.findById(grupoId).orElseThrow(() -> new EntityNotFoundException("Grupo com ID " + grupoId + " não encontrado."));

        Boolean temEmAndamento = projetosClient.existeProjetoEmAndamento(grupoId);
        if (Boolean.TRUE.equals(temEmAndamento)) {
            throw new IllegalStateException("Grupo possui projeto EM_ANDAMENTO. Transfira o projeto antes de excluir.");
        }

        Boolean temPendentes = projetosClient.existeProjetoPendenteAnalise(grupoId);
        if (Boolean.TRUE.equals(temPendentes)) {
            throw new IllegalStateException("Grupo possui projeto pendente de análise. Conclua a análise antes de excluir.");
        }


        grupoRepository.delete(g);
    }


    public List<GrupoResponseDTO> findAll() {
        return grupoRepository.findAll()
                .stream()
                .map(GrupoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public GrupoResponseDTO definirCoordenador(Long grupoId, Long professorId) {
        Grupo g = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new EntityNotFoundException("Grupo com ID " + grupoId + " não encontrado."));

        Boolean existe = pessoasClient.professorExiste(professorId);
        if (Boolean.FALSE.equals(existe) || existe == null) {
            throw new EntityNotFoundException("Professor com ID " + professorId + " não encontrado no serviço de pessoas.");
        }

        g.setCoordenadorId(professorId);

        Grupo grupoSalvo = grupoRepository.save(g);

        return GrupoResponseDTO.fromEntity(grupoSalvo);
    }


}