package com.fernandez.foro_hub.domain.respuesta;

import java.time.LocalDateTime;

public record DatosDetalleRespuesta(
        Long id,
        String mensaje,
        Long topicoId,
        LocalDateTime fechaCreacion,
        Long autorId,
        boolean solucion
) {

    public DatosDetalleRespuesta(Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getTopico().getId(),
                respuesta.getFechaCreacion(),
                respuesta.getAutor().getId(),
                respuesta.isSolucion()
        );
    }
}
