package com.fernandez.foro_hub.domain.topico;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fernandez.foro_hub.domain.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosCreacionTopico(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotNull Long idUsuario,
        @NotNull Long idCurso
        ) {}