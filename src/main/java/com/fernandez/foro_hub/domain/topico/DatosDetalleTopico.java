package com.fernandez.foro_hub.domain.topico;

import com.fernandez.foro_hub.domain.curso.Curso;
import com.fernandez.foro_hub.domain.respuesta.Respuesta;
import com.fernandez.foro_hub.domain.usuario.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public record DatosDetalleTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        Status status,
        Long autorId,
        Long cursoId,
        List<Long> respuestasId
) {

    public DatosDetalleTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor().getId(),
                topico.getCurso().getId(),
                topico.getRespuestas().stream().map(Respuesta::getId).toList()
        );
    }
}