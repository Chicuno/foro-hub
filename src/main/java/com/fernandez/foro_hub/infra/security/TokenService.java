package com.fernandez.foro_hub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fernandez.foro_hub.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")//Debe ser el @Value de Spring.framework y no el de Lombok.
    private String secret;

    public String generarToken(Usuario usuario) {
        //Copiamos el código del repositorio de Auth0 y lo adaptamos a Spring Security.
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Foro.Hub")
                    .withSubject(usuario.getNombreUsuario())
                    .withExpiresAt(fechaExpiracion())
                    .withClaim("id", usuario.getNombre())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error al generar el token JWT", exception);
        }
    }

    private Instant fechaExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-07:00"));
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Foro.Hub")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT inválido o expirado");

        }
    }
}
