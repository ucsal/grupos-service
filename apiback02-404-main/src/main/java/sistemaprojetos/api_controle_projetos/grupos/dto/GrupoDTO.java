package sistemaprojetos.api_controle_projetos.grupos.dto;

import java.time.LocalDate;


public record GrupoDTO(Long id, String nome, String descricao, LocalDate dataCriacao, Boolean disponibilidade, Long coordenadorId) {

}
