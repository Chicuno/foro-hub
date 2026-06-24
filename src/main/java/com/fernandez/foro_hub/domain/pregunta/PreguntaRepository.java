package com.fernandez.foro_hub.domain.pregunta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    Page<Pregunta> findByActivoTrue(Pageable paginacion);
    Page<Pregunta> findByAutorIdAndActivoTrue(Long autorId, Pageable paginacion);
    Page<Pregunta> findByCursoIdAndActivoTrue(Long cursoId, Pageable paginacion);
    Page<Pregunta> findByStatusAndActivoTrue(Status status, Pageable paginacion);
    Optional<Pregunta> findByIdAndActivoTrue(Long id);

    boolean existsByIdAndAutorNombreUsuarioAndActivoTrue(Long preguntaId, String username);
}
