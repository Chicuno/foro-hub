package com.fernandez.foro_hub.domain.pregunta;

import com.fernandez.foro_hub.domain.curso.Curso;
import com.fernandez.foro_hub.domain.respuesta.Respuesta;
import com.fernandez.foro_hub.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "preguntas")
@Entity(name = "Pregunta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Pregunta {

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

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Respuesta> respuestas = new ArrayList<>();

    private boolean activo;

    public void agregarRespuesta(Respuesta respuesta) {
        this.respuestas.add(respuesta);
        respuesta.setPregunta(this);
        this.status = Status.RESPONDIDO;
    }

    public void actualizarInformaciones(@Valid DatosActualizacionPregunta datos) {
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

    @Override
    public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Pregunta)) return false;
    Pregunta pregunta = (Pregunta) o;
    // Si el ID es nulo (no guardado), solo son el mismo objeto si comparten la misma referencia en memoria
    return id != null && id.equals(pregunta.getId());
}

    @Override
    public int hashCode() {
        // Retorna el hash de la clase para mantenerlo constante antes y después del "save"
        return getClass().hashCode(); 
    }

}
