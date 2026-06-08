package com.fernandez.foro_hub.domain.respuesta;

import jakarta.validation.constraints.NotNull;

public record DatosRegistroRespuesta(
@NotNull
String mensaje,
@NotNull
Long topicoId,
@NotNull
Long autorId
) {
}
