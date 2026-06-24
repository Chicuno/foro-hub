package com.fernandez.foro_hub.domain.pregunta;

import jakarta.validation.constraints.NotNull;

public record DatosActualizacionPregunta(
    @NotNull Long id,
    String titulo,
    String mensaje
) {}
