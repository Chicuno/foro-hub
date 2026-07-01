package com.fernandez.foro_hub.infra.security;

import com.fernandez.foro_hub.domain.pregunta.PreguntaRepository;
import com.fernandez.foro_hub.domain.respuesta.RespuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private PreguntaRepository preguntaRepository;
    @Autowired
    private RespuestaRepository respuestaRepository;

    public boolean puedeEditarPregunta(Authentication authentication, Long preguntaId) {
        if (authentication == null) return false;

        boolean isAdminOrProfesor = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                        auth.getAuthority().equals("ROLE_PROFESOR"));

        if (isAdminOrProfesor) {
            return true;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ALUMNO"))
                && esPropietarioDeLaPregunta(preguntaId, authentication);
    }

    public boolean esPropietarioDeLaPregunta(Long preguntaId, Authentication authentication) {
        if (authentication == null || preguntaId == null) return false;
        String username = authentication.getName();
        return preguntaRepository.existsByIdAndAutorNombreUsuarioAndActivoTrue(preguntaId, username);
    }

    public boolean puedeEditarRespuesta(Authentication authentication, Long respuestaId) {
        if (authentication == null) return false;

        boolean isAdminOrProfesor = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                        auth.getAuthority().equals("ROLE_PROFESOR"));

        if (isAdminOrProfesor) {
            return true;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ALUMNO"))
                && esPropietarioDeLaRespuesta(respuestaId, authentication);
    }

    public boolean esPropietarioDeLaRespuesta(Long respuestaId, Authentication authentication) {
        if (authentication == null || respuestaId == null) return false;
        String username = authentication.getName();
        return respuestaRepository.existsByIdAndAutorNombreUsuarioAndActivoTrue(respuestaId, username);
    }

    public boolean puedeEliminarRespuesta(Authentication authentication, Long respuestaId) {
        if (authentication == null) return false;

        boolean isAdminOrProfesor = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                        auth.getAuthority().equals("ROLE_PROFESOR"));

        return isAdminOrProfesor;
    }

    public boolean puedeMarcarSolucion(Authentication authentication, Long respuestaId) {
        if (authentication == null) return false;

        boolean isAdminOrProfesor = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                        auth.getAuthority().equals("ROLE_PROFESOR"));

        if (isAdminOrProfesor) {
            return true;
        }

        // El autor de la pregunta puede marcar una respuesta como solución
        var respuesta = respuestaRepository.findById(respuestaId);
        if (respuesta.isEmpty()) return false;

        String username = authentication.getName();
        return preguntaRepository.existsByIdAndAutorNombreUsuarioAndActivoTrue(respuesta.get().getPregunta().getId(), username);
    }
}
