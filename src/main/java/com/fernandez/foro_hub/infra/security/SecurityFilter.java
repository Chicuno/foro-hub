package com.fernandez.foro_hub.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fernandez.foro_hub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //No es específico, sólo ocupamos que Spring lo cargue.
public class SecurityFilter extends OncePerRequestFilter {//Extendemos de Once y luego creamos el método.

    @Autowired
    private UsuarioRepository repository;//Inyectamos el repositorio.

    @Autowired
    private TokenService tokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);//El subject es el login.
            var usuario = repository.findByNombreUsuario(subject);//Buscamos el usuario en la base de datos.
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());//UsernamePasswordAuthenticationToken es el mismo que usamos en AutenticacionController para decirle a Spring que los datos que nos están enviando pertenecen a éste usuario, que es como un DTO de Spring para poder saber cuál es el usuario autenticado. Como parámetros pasamos el usuario, null como contraseña, pues ya viene dentro de usuario y el rol o políticas que tiene ese usuario que con usuario.getAuthorities() obtenemos, que son las propiedades que definimos en la clase Usuario (entidad JPA).
            SecurityContextHolder.getContext().setAuthentication(authentication);//Establecemos que el usuario está autenticado. El usuario viene dentro de authentication.

        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
