package com.fernandez.foro_hub.controller;

import jakarta.validation.Valid;
import com.fernandez.foro_hub.domain.usuario.DatosAutenticacion;
import com.fernandez.foro_hub.domain.usuario.Usuario;
import com.fernandez.foro_hub.infra.security.DatosTokenJWT;
import com.fernandez.foro_hub.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity iniciarSesion(@RequestBody @Valid DatosAutenticacion datos) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.nombreUsuario(), datos.contrasena());
        var autenticacion = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.generarToken((Usuario) autenticacion.getPrincipal());
        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}
