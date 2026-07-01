package com.fernandez.foro_hub.domain.pregunta;

import com.fernandez.foro_hub.domain.respuesta.Respuesta;
import java.time.LocalDateTime;
import java.util.List;

public record DatosDetallePregunta(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        Status status,
        Long autorId,
        String autorNombre,
        Long cursoId,
        List<Long> respuestasId
) {

    public DatosDetallePregunta(Pregunta pregunta) {
        this(
                pregunta.getId(),
                pregunta.getTitulo(),
                pregunta.getMensaje(),
                pregunta.getFechaCreacion(),
                pregunta.getStatus(),
                pregunta.getAutor().getId(),
                pregunta.getAutor().getNombreUsuario(),
                pregunta.getCurso().getId(),
                pregunta.getRespuestas().stream().map(Respuesta::getId).toList()
        );
    }
}
