package com.fernandez.foro_hub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    Page<Topico> findByActivoTrue(Pageable paginacion);
    Page<Topico> findByAutorIdAndActivoTrue(Long autorId, Pageable paginacion);
    Page<Topico> findByCursoIdAndActivoTrue(Long cursoId, Pageable paginacion);
    Page<Topico> findByStatusAndActivoTrue(Status status, Pageable paginacion);
    Optional<Topico> findByIdAndActivoTrue(Long id);

    boolean existsByIdAndAutorNombreUsuarioAndActivoTrue(Long topicoId, String username);
}
