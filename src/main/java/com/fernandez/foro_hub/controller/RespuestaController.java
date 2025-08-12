package com.fernandez.foro_hub.controller;

import com.fernandez.foro_hub.domain.respuesta.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/respuestas")

public class RespuestaController {
    @Autowired RespuestaService service;
    @Autowired
    private RespuestaRepository repository;

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
}
