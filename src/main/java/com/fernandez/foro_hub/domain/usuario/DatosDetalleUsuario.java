package com.fernandez.foro_hub.domain.usuario;

public record DatosDetalleUsuario(
        Long id,
        String nombre,
        String correoElectronico,
        String nombreUsuario,
        Perfil perfil
){
    public DatosDetalleUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreoElectronico(),
                usuario.getNombreUsuario(),
                usuario.getPerfil()
        );
    }
}
