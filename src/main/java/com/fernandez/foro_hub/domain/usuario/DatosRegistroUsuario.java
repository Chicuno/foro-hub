package com.fernandez.foro_hub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroUsuario(
        @NotBlank String nombre,
        @NotBlank @Email String correoElectronico,
        @NotBlank String nombreUsuario,
        @NotBlank String contrasena,
        @NotNull Perfil perfil
) {}