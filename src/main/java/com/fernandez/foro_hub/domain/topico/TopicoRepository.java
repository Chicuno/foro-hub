package com.fernandez.foro_hub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    Page<Topico> findByAutorId(Long autorId, Pageable paginacion);
    Page<Topico> findByCursoId(Long cursoId, Pageable paginacion);
    Page<Topico> findByStatus(Status status, Pageable paginacion);
}

