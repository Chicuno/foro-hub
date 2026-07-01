package com.fernandez.foro_hub.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByNombreUsuarioAndActivoTrue(String nombreUsuario);
    Usuario findByNombreUsuarioIgnoreCaseAndActivoTrue(String nombreUsuario);
    Page<Usuario> findByActivoTrue(Pageable paginacion);
    Optional<Usuario> findByIdAndActivoTrue(Long id);
}
