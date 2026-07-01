package com.fernandez.foro_hub.domain.respuesta;

import com.fernandez.foro_hub.domain.pregunta.Pregunta;
import com.fernandez.foro_hub.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "respuestas")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pregunta_id")
    private Pregunta pregunta;

    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    private boolean solucion;

    private boolean activo = true;

    @Column(name = "activo")
    public boolean isActivo() {
        return activo;
    }

    public void eliminar() {
        this.activo = false;
    }

    public void marcarComoSolucion() {
        this.solucion = true;
    }

    public void actualizarInformaciones(@Valid DatosActualizacionRespuesta datos) {
        if(datos.mensaje() != null) {
            this.mensaje = datos.mensaje();
        }
    }

    @Override
    public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Respuesta)) return false;
    Respuesta respuesta = (Respuesta) o;
    return id != null && id.equals(respuesta.getId());
    }

    @Override
    public int hashCode() {
    // Mantiene el hash constante sin importar si el ID pasa de null a un número tras el save()
    return getClass().hashCode();
    }
}
