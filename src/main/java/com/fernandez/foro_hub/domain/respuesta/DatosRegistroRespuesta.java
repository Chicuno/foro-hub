package com.fernandez.foro_hub.domain.respuesta;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DatosRegistroRespuesta(
@NotNull
String mensaje,
@NotNull
Long topicoId,
@NotNull
Long autorId
) {
}
