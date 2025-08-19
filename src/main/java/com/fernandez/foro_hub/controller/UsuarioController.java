package com.fernandez.foro_hub.controller;

import com.fernandez.foro_hub.domain.topico.DatosListaTopico;
import com.fernandez.foro_hub.domain.topico.TopicoRepository;
import com.fernandez.foro_hub.domain.usuario.DatosDetalleUsuario;
import com.fernandez.foro_hub.domain.usuario.DatosRegistroUsuario;
import com.fernandez.foro_hub.domain.usuario.Usuario;
import com.fernandez.foro_hub.domain.usuario.UsuarioRepository;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired private UsuarioRepository repository;
    @Autowired private TopicoRepository topicoRepository;

    @Transactional
    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroUsuario datos, UriComponentsBuilder uriComponentsBuilder) {
        var usuario = new Usuario(datos);
        repository.save(usuario);
        var uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosDetalleUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<DatosDetalleUsuario>> listar(@PageableDefault(size=10, sort={"nombre"}) Pageable paginacion){

        var page = repository.findAll(paginacion)
                .map(DatosDetalleUsuario::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{usuarioId}/topicos")
    public ResponseEntity<Page<DatosListaTopico>> listarTopicosDeUsuario(@PathVariable Long usuarioId, @PageableDefault(size=10, sort={"fechaCreacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        var page = topicoRepository.findByAutorId(usuarioId, paginacion)
                .map(DatosListaTopico::new);
        return ResponseEntity.ok(page);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id) {
        var usuario = repository.getReferenceById(id);
        usuario.eliminar();
        return ResponseEntity.noContent().build();
    }
}
