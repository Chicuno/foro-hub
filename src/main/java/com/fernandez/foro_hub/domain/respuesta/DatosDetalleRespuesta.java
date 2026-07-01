package com.fernandez.foro_hub.domain.respuesta;

import java.time.LocalDateTime;

public record DatosDetalleRespuesta(
        Long id,
        String mensaje,
        Long preguntaId,
        LocalDateTime fechaCreacion,
        Long autorId,
        String autorNombre,
        boolean solucion
) {

    public DatosDetalleRespuesta(Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getPregunta().getId(),
                respuesta.getFechaCreacion(),
                respuesta.getAutor().getId(),
                respuesta.getAutor().getNombreUsuario(),
                respuesta.isSolucion()
        );
    }
}
