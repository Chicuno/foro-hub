package com.fernandez.foro_hub.infra.security;

import com.fernandez.foro_hub.domain.topico.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private TopicoRepository topicoRepository;

    public boolean puedeEditarTopico(Authentication authentication, Long topicoId) {
        if (authentication == null) return false;

        boolean isAdminOrProfesor = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                        auth.getAuthority().equals("ROLE_PROFESOR"));

        if (isAdminOrProfesor) {
            return true;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ALUMNO"))
                && esPropietarioDelTopico(topicoId, authentication);
    }

    public boolean esPropietarioDelTopico(Long topicoId, Authentication authentication) {
        if (authentication == null || topicoId == null) return false;
        String username = authentication.getName();
        return topicoRepository.existsByIdAndAutorNombreUsuario(topicoId, username);
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
        return topicoRepository.existsByIdAndAutorNombreUsuario(respuestaId, username);
    }
}