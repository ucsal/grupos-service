package sistemaprojetos.api_controle_projetos.grupos.dto;


import sistemaprojetos.api_controle_projetos.grupos.model.entity.Grupo;
import java.util.List;

public record GrupoResponseDTO(
        Long id,
        String nome,
        String descricao,
        Boolean disponibilidade,
        Long coordenadorId,
        List<Long> alunos
) {
    public static GrupoResponseDTO fromEntity(Grupo grupo) {
        return new GrupoResponseDTO(
                grupo.getId(),
                grupo.getNome(),
                grupo.getDescricao(),
                grupo.getDisponibilidade(),
                grupo.getCoordenadorId(),
               grupo.getAlunosIds()
        );
    }




}