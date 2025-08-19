package com.fernandez.foro_hub.controller;

import com.fernandez.foro_hub.domain.curso.CursoRepository;
import com.fernandez.foro_hub.domain.topico.*;
import com.fernandez.foro_hub.domain.usuario.UsuarioRepository;
import com.fernandez.foro_hub.infra.security.SecurityService;
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
    @Autowired
    private SecurityService securityService;

    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosCreacionTopico datos, UriComponentsBuilder uriComponentsBuilder) {
        var detallesTopico = service.crear(datos);
        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(detallesTopico.id()).toUri();
        return ResponseEntity.created(uri).body(detallesTopico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaTopico>> listar(@PageableDefault(size=10, sort={"fechaCreacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        var page = service.listar(paginacion);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/antiguos-primero")
    public ResponseEntity<Page<DatosListaTopico>> listarAntiguosPrimero(@PageableDefault(size=10, sort={"fechaCreacion"}) Pageable paginacion){
        var page = service.listar(paginacion);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detallar(@PathVariable Long id) {
        var topico = service.detallar(id);
        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    @GetMapping("/por-status/{status}")
    public ResponseEntity<Page<DatosListaTopico>> listarPorStatus(@PathVariable Status status, @PageableDefault(size=10, sort={"fechaCreacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        var page = service.listarPorStatus(status, paginacion);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @PreAuthorize("@securityService.puedeEditarTopico(authentication, #datos.id())")
    public ResponseEntity actualizar(@RequestBody @Valid DatosActualizacionTopico datos) {
        var topico = service.actualizar(datos);
        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
