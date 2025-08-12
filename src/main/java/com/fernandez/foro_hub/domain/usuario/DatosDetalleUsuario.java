package com.fernandez.foro_hub.domain.usuario;

import com.fernandez.foro_hub.domain.respuesta.Respuesta;
import com.fernandez.foro_hub.domain.topico.Topico;

import java.util.List;

public record DatosDetalleUsuario(
        Long id,
        String nombre,
        String correoElectronico,
        String nombreUsuario,
        Perfil perfil,
        List<Long> topicosIds,
        List<Long>respuestasIds
){
    public DatosDetalleUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreoElectronico(),
                usuario.getNombreUsuario(),
                usuario.getPerfil(),
                usuario.getTopicos().stream().map(Topico::getId).toList(),
                usuario.getRespuestas().stream().map(Respuesta::getId).toList()
        );
    }
}
