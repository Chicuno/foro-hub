package com.fernandez.foro_hub.controller;

import com.fernandez.foro_hub.domain.ValidacionException;
import com.fernandez.foro_hub.domain.curso.Curso;
import com.fernandez.foro_hub.domain.curso.CursoRepository;
import com.fernandez.foro_hub.domain.curso.DatosDetalleCurso;
import com.fernandez.foro_hub.domain.curso.DatosRegistroCurso;
import com.fernandez.foro_hub.domain.pregunta.DatosListaPregunta;
import com.fernandez.foro_hub.domain.pregunta.PreguntaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cursos")

public class CursoController {

    @Autowired private CursoRepository repository;
    @Autowired private PreguntaRepository preguntaRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<DatosDetalleCurso> registrar(@RequestBody @Valid DatosRegistroCurso datos, UriComponentsBuilder uriComponentsBuilder) {
        var curso = new Curso(datos);
        repository.save(curso);
        var uri = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosDetalleCurso(curso));
    }
    @GetMapping
    public ResponseEntity<Page<DatosDetalleCurso>> listar(@PageableDefault(size=10, sort={"nombre"}) Pageable paginacion){

        var page = repository.findAll(paginacion)
                .map(DatosDetalleCurso::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{cursoId}")
    public ResponseEntity<DatosDetalleCurso> obtenerCurso(@PathVariable Long cursoId) {
        var curso = repository.findById(cursoId)
                .orElseThrow(() -> new ValidacionException("Curso no encontrado"));
        return ResponseEntity.ok(new DatosDetalleCurso(curso));
    }

    @GetMapping("/{cursoId}/preguntas")
    @Transactional(readOnly = true)
    public ResponseEntity<Page<DatosListaPregunta>> listarPreguntasDelCurso(@PathVariable Long cursoId, @PageableDefault(size=10, sort={"fechaCreacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        var page = preguntaRepository.findByCursoIdAndActivoTrue(cursoId, paginacion)
                .map(DatosListaPregunta::new);
        return ResponseEntity.ok(page);
    }

}
