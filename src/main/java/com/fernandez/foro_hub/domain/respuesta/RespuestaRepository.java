package com.fernandez.foro_hub.domain.respuesta;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    boolean existsByIdAndAutorNombreUsuario(Long respuestaId, String username);
}
