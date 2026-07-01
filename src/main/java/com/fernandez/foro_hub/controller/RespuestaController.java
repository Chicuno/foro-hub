package com.fernandez.foro_hub.controller;

import com.fernandez.foro_hub.domain.respuesta.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/respuestas")

public class RespuestaController {
    @Autowired
    private RespuestaService service;
    @Autowired
    private RespuestaRepository repository;

    @Transactional
    @PostMapping
    public ResponseEntity<DatosDetalleRespuesta> registrar(@RequestBody @Valid DatosRegistroRespuesta datos, UriComponentsBuilder uriComponentsBuilder) {
        var detallesRespuesta = service.crear(datos);
        var uri = uriComponentsBuilder.path("/respuestas/{id}").buildAndExpand(detallesRespuesta.id()).toUri();

        return ResponseEntity.created(uri).body(detallesRespuesta);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Page<DatosDetalleRespuesta>> listar(@PageableDefault(size=10, sort={"fechaCreacion"}) Pageable paginacion){

        var page = repository.findAll(paginacion)
                .map(DatosDetalleRespuesta::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<DatosDetalleRespuesta> detallar(@PathVariable Long id) {
        var respuesta = service.detallar(id);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/pregunta/{preguntaId}")
    @Transactional(readOnly = true)
    public ResponseEntity<Page<DatosDetalleRespuesta>> listarPorPregunta(@PathVariable Long preguntaId, @PageableDefault(size=10, sort={"fechaCreacion"}) Pageable paginacion){
        var page = service.listarPorPregunta(preguntaId, paginacion);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @PreAuthorize("@securityService.puedeEditarRespuesta(authentication, #datos.id())")
    public ResponseEntity<DatosDetalleRespuesta> actualizar(@RequestBody @Valid DatosActualizacionRespuesta datos) {
        var respuesta = service.actualizar(datos);
        return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.puedeEliminarRespuesta(authentication, #id)")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/solucion")
    @PreAuthorize("@securityService.puedeMarcarSolucion(authentication, #id)")
    @Transactional
    public ResponseEntity<Void> marcarComoSolucion(@PathVariable Long id) {
        service.marcarComoSolucion(id);
        return ResponseEntity.noContent().build();
    }
}
