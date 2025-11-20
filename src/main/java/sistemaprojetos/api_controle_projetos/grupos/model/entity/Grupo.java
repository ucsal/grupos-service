package sistemaprojetos.api_controle_projetos.grupos.model.entity;

import sistemaprojetos.api_controle_projetos.grupos.dto.GrupoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="grupo")

public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column
    private String descricao;

    @Column
    private LocalDate dataCriacao;

    @Column
    private Boolean disponibilidade;

    @ElementCollection
    @CollectionTable(name = "grupo_alunos", joinColumns = @JoinColumn(name = "grupo_id"))
    @Column(name = "aluno_id")
    private List<Long> alunosIds = new ArrayList<>();


    @Column(name= "coordenador_id")
    private Long coordenadorId;

    public Grupo(GrupoDTO dto) {
        this.nome = dto.nome();
        this.descricao = dto.descricao();
        this.dataCriacao = LocalDate.now();
        this.disponibilidade = true;
        this.alunosIds = new ArrayList<>();
        this.coordenadorId = dto.coordenadorId();

    }

    public static Grupo fromId(Long id) {
        if (id == null) return null;
        Grupo proxy = new Grupo();
        proxy.setId(id);
        return proxy;
    }

}
