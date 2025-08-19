package com.fernandez.foro_hub.controller;

import com.fernandez.foro_hub.domain.respuesta.*;
import com.fernandez.foro_hub.infra.security.SecurityService;
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
    @Autowired
    private SecurityService securityService;

    @Transactional
    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroRespuesta datos, UriComponentsBuilder uriComponentsBuilder) {
        var detallesRespuesta = service.crear(datos);
        var uri = uriComponentsBuilder.path("/respuestas/{id}").buildAndExpand(detallesRespuesta.id()).toUri();

        return ResponseEntity.created(uri).body(detallesRespuesta);
    }

    @GetMapping
    public ResponseEntity<Page<DatosDetalleRespuesta>> listar(@PageableDefault(size=10, sort={"fechaCreacion"}) Pageable paginacion){

        var page = repository.findAll(paginacion)
                .map(DatosDetalleRespuesta::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detallar(@PathVariable Long id) {
        var respuesta = service.detallar(id);
        return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
    }

    @PutMapping
    @PreAuthorize("@securityService.puedeEditarRespuesta(authentication, #datos.id())")
    public ResponseEntity actualizar(@RequestBody @Valid DatosActualizacionRespuesta datos) {
        var respuesta = service.actualizar(datos);
        return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
    }
}
