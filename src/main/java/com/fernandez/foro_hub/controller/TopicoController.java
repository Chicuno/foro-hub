package com.fernandez.foro_hub.controller;

import com.fernandez.foro_hub.domain.curso.CursoRepository;
import com.fernandez.foro_hub.domain.topico.*;
import com.fernandez.foro_hub.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")

public class TopicoController {

    @Autowired
    TopicoService service;
    @Autowired
    TopicoRepository repository;
    @Autowired
    CursoRepository cursoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DatosListaTopico> registrar(@RequestBody @Valid DatosCreacionTopico datos, UriComponentsBuilder uriComponentsBuilder) {
        var topicoCreado = service.crear(datos);
        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topicoCreado.id()).toUri();
        return ResponseEntity.created(uri).body(topicoCreado);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaTopico>> listar(@PageableDefault(size=10, sort={"fechaCreacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        Page<DatosListaTopico> page = service.listar(paginacion);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/antiguos-primero")
    public ResponseEntity<Page<DatosListaTopico>> listarAntiguosPrimero(@PageableDefault(size=10, sort={"fechaCreacion"}) Pageable paginacion){
        Page<DatosListaTopico> page = service.listar(paginacion);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> detallar(@PathVariable Long id) {
        var topico = service.detallar(id);
        return ResponseEntity.ok(topico);
    }

    @GetMapping("/por-status/{status}")
    public ResponseEntity<Page<DatosListaTopico>> listarPorStatus(@PathVariable Status status, @PageableDefault(size=10, sort={"fechaCreacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        var page = service.listarPorStatus(status, paginacion);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @PreAuthorize("@securityService.puedeEditarTopico(authentication, #datos.id())")
    public ResponseEntity<DatosDetalleTopico> actualizar(@RequestBody @Valid DatosActualizacionTopico datos) {
        var topico = service.actualizar(datos);
        return ResponseEntity.ok(topico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
