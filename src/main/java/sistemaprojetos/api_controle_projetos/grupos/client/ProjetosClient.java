package sistemaprojetos.api_controle_projetos.grupos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "projetos", path = "/api/projetos")

public interface ProjetosClient {

    @GetMapping("/grupos/{grupoId}/existe-em-andamento")
    Boolean existeProjetoEmAndamento(@PathVariable("grupoId") Long grupoId);

    @GetMapping("/grupos/{grupoId}/existe-pendente-analise")
    Boolean existeProjetoPendenteAnalise(@PathVariable("grupoId") Long grupoId);
}
