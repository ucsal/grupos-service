package sistemaprojetos.api_controle_projetos.grupos.model.repository;

import sistemaprojetos.api_controle_projetos.grupos.model.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    List<Grupo> findByDisponibilidadeTrue();
    boolean existsByNomeIgnoreCase(String nome);

}
