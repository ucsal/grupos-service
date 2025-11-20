package sistemaprojetos.api_controle_projetos.grupos.dto;

import sistemaprojetos.api_controle_projetos.grupos.model.entity.Grupo;

public record GrupoSimplificadoDTO(
        Long id,
        String nome
) {
    public static GrupoSimplificadoDTO fromEntity(Grupo grupo) {

        if (grupo == null) {
            return null;
        }
        return new GrupoSimplificadoDTO(
                grupo.getId(),
                grupo.getNome()
        );
    }
}