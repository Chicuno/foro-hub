package com.fernandez.foro_hub.domain.pregunta;

import java.time.LocalDateTime;

public record DatosListaPregunta(
        Long id,
        String titulo,
        LocalDateTime fechaCreacion,
        Long autorId,
        Long cursoId
) {
    public DatosListaPregunta(Pregunta pregunta) {
        this(
                pregunta.getId(),
                pregunta.getTitulo(),
                pregunta.getFechaCreacion(),
                pregunta.getAutor().getId(),
                pregunta.getCurso().getId()
        );
    }
}
