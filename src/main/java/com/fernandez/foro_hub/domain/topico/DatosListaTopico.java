package com.fernandez.foro_hub.domain.topico;

import java.time.LocalDateTime;

public record DatosListaTopico(
        Long id,
        String titulo,
        LocalDateTime fechaCreacion,
        Long autorId,
        Long cursoId
) {
    public DatosListaTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getFechaCreacion(),
                topico.getAutor().getId(),
                topico.getCurso().getId()
        );
    }
}
