package com.fernandez.foro_hub.domain.respuesta;

import com.fernandez.foro_hub.domain.topico.DatosActualizacionTopico;
import com.fernandez.foro_hub.domain.topico.Topico;
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
@EqualsAndHashCode(of = "id")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    private boolean solucion;


    public void marcarComoSolucion() {
        this.solucion = true;
    }

    public void actualizarInformaciones(@Valid DatosActualizacionRespuesta datos) {
        if(datos.mensaje() != null) {
            this.mensaje = datos.mensaje();
        }
    }
}
