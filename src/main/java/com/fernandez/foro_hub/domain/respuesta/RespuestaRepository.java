package com.fernandez.foro_hub.domain.respuesta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    boolean existsByIdAndAutorNombreUsuario(Long respuestaId, String username);
    boolean existsByIdAndAutorNombreUsuarioAndActivoTrue(Long respuestaId, String username);
    Page<Respuesta> findByPreguntaIdAndActivoTrue(Long preguntaId, Pageable paginacion);
    Respuesta findByIdAndActivoTrue(Long id);
}
