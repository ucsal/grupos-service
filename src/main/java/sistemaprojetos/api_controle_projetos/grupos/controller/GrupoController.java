package sistemaprojetos.api_controle_projetos.grupos.controller;

import sistemaprojetos.api_controle_projetos.grupos.dto.GrupoDTO;
import sistemaprojetos.api_controle_projetos.grupos.dto.GrupoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sistemaprojetos.api_controle_projetos.grupos.model.service.GrupoService;
import java.util.List;


@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;


        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping("/criar")
        public GrupoResponseDTO create(@RequestBody GrupoDTO dto) {
            return grupoService.create(dto);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/atualizar/{id}")
        public GrupoResponseDTO update(@PathVariable Long id, @RequestBody GrupoDTO dto) {
            return grupoService.update(id, dto);
        }

        @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
        @GetMapping("/listar-um/{id}")
        public GrupoResponseDTO findById(@PathVariable Long id) {
            return grupoService.findById(id);
        }

        @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
        @GetMapping("/listar-disponiveis")
        public List<GrupoResponseDTO> listarDisponiveis() {
            return grupoService.listarDisponiveis();
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/definir-disponibilidade/{id}")
        public GrupoResponseDTO definirDisponibilidade(@PathVariable Long id, @RequestParam boolean disponivel) {
            return grupoService.definirDisponibilidade(id, disponivel);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/adicionar-alunos/{id}")
        public GrupoResponseDTO adicionarAlunos(@PathVariable Long id, @RequestBody List<Long> alunosIds) {
            return grupoService.adicionarAlunos(id, alunosIds);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/remover-alunos/{id}")
        public GrupoResponseDTO removerAlunos(@PathVariable Long id, @RequestBody List<Long> alunosIds) {
            return grupoService.removerAlunos(id, alunosIds);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/substituir-alunos/{id}")
        public GrupoResponseDTO substituirAlunos(@PathVariable Long id, @RequestBody List<Long> alunosIds) {
            return grupoService.substituirAlunos(id, alunosIds);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/excluir/{id}")
        public void delete(@PathVariable Long id) {
            grupoService.delete(id);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/listar-todos")
        public List<GrupoResponseDTO> findAll(){
            return grupoService.findAll();
        }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/definir-coordenador/{id}")
    public GrupoResponseDTO definirCoordenador(@PathVariable Long id, @RequestParam Long professorId) {
        return grupoService.definirCoordenador(id, professorId);
    }

}