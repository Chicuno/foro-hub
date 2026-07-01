package com.fernandez.foro_hub.controller;

import com.fernandez.foro_hub.domain.pregunta.DatosListaPregunta;
import com.fernandez.foro_hub.domain.pregunta.PreguntaRepository;
import com.fernandez.foro_hub.domain.ValidacionException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired 
    private UsuarioRepository repository;
    @Autowired 
    private PreguntaRepository preguntaRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<DatosDetalleUsuario> registrar(@RequestBody @Valid DatosRegistroUsuario datos, UriComponentsBuilder uriComponentsBuilder) {
        var usuario = new Usuario(datos);
        usuario.setContrasena(passwordEncoder.encode(datos.contrasena()));
        repository.save(usuario);
        var uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosDetalleUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<DatosDetalleUsuario>> listar(@PageableDefault(size=10, sort={"nombre"}) Pageable paginacion){

        var page = repository.findByActivoTrue(paginacion)
                .map(DatosDetalleUsuario::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{usuarioId}/preguntas")
    @Transactional(readOnly = true)
    public ResponseEntity<Page<DatosListaPregunta>> listarPreguntasDeUsuario(@PathVariable Long usuarioId, @PageableDefault(size=10, sort={"fechaCreacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        var page = preguntaRepository.findByAutorIdAndActivoTrue(usuarioId, paginacion)
                .map(DatosListaPregunta::new);
        return ResponseEntity.ok(page);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        var usuario = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ValidacionException("Id del usuario informado no existe"));
        usuario.eliminar();
        return ResponseEntity.noContent().build();
    }
}
