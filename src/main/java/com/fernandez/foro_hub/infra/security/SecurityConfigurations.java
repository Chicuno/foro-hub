package com.fernandez.foro_hub.infra.security;

import com.fernandez.foro_hub.domain.topico.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;


@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Indicamos que no creamos sesiones, es un sistema stateless. No creamos sesiones, no guardamos datos en el navegador, no guardamos cookies, etc.
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()

                        .requestMatchers(HttpMethod.POST, "/cursos").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/cursos").hasAnyRole("ADMINISTRADOR", "PROFESOR", "ALUMNO")
                        .requestMatchers(HttpMethod.GET, "/cursos/{id}").hasAnyRole("ADMINISTRADOR", "PROFESOR", "ALUMNO")

                        .requestMatchers(HttpMethod.POST, "/usuarios").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasAnyRole("ADMINISTRADOR", "PROFESOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{usuarioId}/topicos").hasAnyRole("ADMINISTRADOR", "PROFESOR", "ALUMNO")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/{id}").hasAnyRole("ADMINISTRADOR", "PROFESOR")

                        .requestMatchers(HttpMethod.POST, "/topicos").hasAnyRole("ADMINISTRADOR", "PROFESOR", "ALUMNO")
                        .requestMatchers(HttpMethod.GET, "/topicos").hasAnyRole("ADMINISTRADOR", "PROFESOR", "ALUMNO")
                        .requestMatchers(HttpMethod.GET, "/topicos/{id}").hasAnyRole("ADMINISTRADOR", "PROFESOR", "ALUMNO")
                        .requestMatchers(HttpMethod.GET, "/topicos/{id}/respuestas").hasAnyRole("ADMINISTRADOR", "PROFESOR", "ALUMNO")
                        .requestMatchers(HttpMethod.PUT, "/topicos/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/topicos").hasAnyRole("ADMIN", "PROFESOR")

                        .requestMatchers(HttpMethod.POST, "/respuestas").hasAnyRole("ADMINISTRADOR", "PROFESOR", "ALUMNO")
                        .requestMatchers(HttpMethod.GET, "/respuestas").hasAnyRole("ADMINISTRADOR", "PROFESOR", "ALUMNO")
                        .requestMatchers(HttpMethod.GET, "/respuestas/{id}").hasAnyRole("ADMINISTRADOR", "PROFESOR", "ALUMNO")
                        .requestMatchers(HttpMethod.PUT, "/respuestas/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/respuestas").hasAnyRole("ADMINISTRADOR", "PROFESOR")

                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @EnableMethodSecurity
    public class MethodSecurityConfig {
        @Bean
        public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
            return new DefaultMethodSecurityExpressionHandler();
        }
    }
}

