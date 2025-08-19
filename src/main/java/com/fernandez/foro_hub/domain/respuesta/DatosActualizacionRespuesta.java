package com.fernandez.foro_hub.domain.respuesta;

import jakarta.validation.constraints.NotNull;

public record DatosActualizacionRespuesta(
    @NotNull Long id,
    String mensaje
) {}
