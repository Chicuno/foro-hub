package com.fernandez.foro_hub.domain.curso;

import com.fernandez.foro_hub.domain.topico.Topico;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "cursos")
@Entity(name = "Curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Topico> topicos = new ArrayList<>();

    public Curso(DatosRegistroCurso datos) {
        this.nombre = datos.nombre();
        this.categoria = datos.categoria();
    }

    public void agregarTopico(Topico topico) {
        this.topicos.add(topico);
        topico.setCurso(this);
    }
}

