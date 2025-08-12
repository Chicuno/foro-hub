package com.fernandez.foro_hub.domain.topico;

import com.fernandez.foro_hub.domain.curso.Curso;
import com.fernandez.foro_hub.domain.respuesta.Respuesta;
import com.fernandez.foro_hub.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String mensaje;

    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Respuesta> respuestas = new ArrayList<>();

    private boolean activo;

    public void agregarRespuesta(Respuesta respuesta) {
        this.respuestas.add(respuesta);
        respuesta.setTopico(this);
        this.status = Status.RESPONDIDO;
    }

    public void actualizarInformaciones(@Valid DatosActualizacionTopico datos) {
        if(datos.titulo() != null) {
            this.titulo = datos.titulo();
        }
        if (datos.mensaje() != null) {
            this.mensaje = datos.mensaje();
        }
    }

    public void eliminar() {
        this.activo = false;
    }

}
