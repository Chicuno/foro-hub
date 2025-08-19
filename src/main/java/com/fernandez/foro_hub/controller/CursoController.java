package com.fernandez.foro_hub.controller;

import com.fernandez.foro_hub.domain.curso.Curso;
import com.fernandez.foro_hub.domain.curso.CursoRepository;
import com.fernandez.foro_hub.domain.curso.DatosDetalleCurso;
import com.fernandez.foro_hub.domain.curso.DatosRegistroCurso;
import com.fernandez.foro_hub.domain.topico.DatosListaTopico;
import com.fernandez.foro_hub.domain.topico.TopicoRepository;
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
    @Autowired private TopicoRepository topicoRepository;

    @Transactional
    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroCurso datos, UriComponentsBuilder uriComponentsBuilder) {
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

    @GetMapping("/{cursoId}/topicos")
    public ResponseEntity<Page<DatosListaTopico>> listarTopicosDelCurso(@PathVariable Long cursoId, @PageableDefault(size=10, sort={"fechaCreacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        var page = topicoRepository.findByCursoId(cursoId, paginacion)
                .map(DatosListaTopico::new);
        return ResponseEntity.ok(page);
    }

}
