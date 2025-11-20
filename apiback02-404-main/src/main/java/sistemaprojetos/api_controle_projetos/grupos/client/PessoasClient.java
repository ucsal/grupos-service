package sistemaprojetos.api_controle_projetos.grupos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "pessoas", path = "/api/pessoas")

public interface PessoasClient {

    @GetMapping("/professores/{id}/existe")
    Boolean professorExiste(@PathVariable("id") Long professorId);

    @PostMapping("/alunos/ids-faltantes")
    List<Long> buscarIdsAlunosInexistentes(@RequestBody List<Long> idsAlunos);
}
