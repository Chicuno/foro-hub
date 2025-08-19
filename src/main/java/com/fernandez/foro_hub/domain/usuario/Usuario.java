package com.fernandez.foro_hub.domain.usuario;

import com.fernandez.foro_hub.domain.respuesta.Respuesta;
import com.fernandez.foro_hub.domain.topico.Topico;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(unique = true)
    private String correoElectronico;
    private String nombreUsuario;
    private String contrasena;
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil")
    private Perfil perfil;
    private boolean activo = true;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Topico> topicos = new ArrayList<>();

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Respuesta> respuestas = new ArrayList<>();

    public Usuario(@Valid DatosRegistroUsuario datos) {
        this.nombre = datos.nombre();
        this.correoElectronico = datos.correoElectronico();
        this.nombreUsuario = datos.nombreUsuario();
        this.contrasena = datos.contrasena();
        this.perfil = datos.perfil();
    }

    public void agregarTopico(Topico topico) {
        this.topicos.add(topico);
        topico.setAutor(this);
    }

    public void agregarRespuesta(Respuesta respuesta) {
        this.respuestas.add(respuesta);
        respuesta.setAutor(this);
    }

    public void eliminar() {
        this.activo = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.perfil.name()));
    }

    @Override
    public String getPassword() {
        return this.contrasena;
    }

    @Override
    public String getUsername() {
        return this.nombreUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.activo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.activo;
    }
}