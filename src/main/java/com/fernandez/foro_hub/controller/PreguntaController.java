package com.fernandez.foro_hub.controller;

import com.fernandez.foro_hub.domain.curso.CursoRepository;
import com.fernandez.foro_hub.domain.pregunta.*;
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
@RequestMapping("/preguntas")

public class PreguntaController {

    @Autowired
    PreguntaService service;
    @Autowired
    PreguntaRepository repository;
    @Autowired
    CursoRepository cursoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DatosListaPregunta> registrar(@RequestBody @Valid DatosCreacionPregunta datos, UriComponentsBuilder uriComponentsBuilder) {
        var preguntaCreado = service.crear(datos);
        var uri = uriComponentsBuilder.path("/preguntas/{id}").buildAndExpand(preguntaCreado.id()).toUri();
        return ResponseEntity.created(uri).body(preguntaCreado);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaPregunta>> listar(@PageableDefault(size=10, sort={"fechaCreacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        Page<DatosListaPregunta> page = service.listar(paginacion);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/antiguos-primero")
    public ResponseEntity<Page<DatosListaPregunta>> listarAntiguosPrimero(@PageableDefault(size=10, sort={"fechaCreacion"}) Pageable paginacion){
        Page<DatosListaPregunta> page = service.listar(paginacion);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetallePregunta> detallar(@PathVariable Long id) {
        var pregunta = service.detallar(id);
        return ResponseEntity.ok(pregunta);
    }

    @GetMapping("/por-status/{status}")
    public ResponseEntity<Page<DatosListaPregunta>> listarPorStatus(@PathVariable Status status, @PageableDefault(size=10, sort={"fechaCreacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        var page = service.listarPorStatus(status, paginacion);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @PreAuthorize("@securityService.puedeEditarPregunta(authentication, #datos.id())")
    public ResponseEntity<DatosDetallePregunta> actualizar(@RequestBody @Valid DatosActualizacionPregunta datos) {
        var pregunta = service.actualizar(datos);
        return ResponseEntity.ok(pregunta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
