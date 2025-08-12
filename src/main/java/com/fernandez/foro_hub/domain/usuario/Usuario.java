package com.fernandez.foro_hub.domain.usuario;

import com.fernandez.foro_hub.domain.respuesta.Respuesta;
import com.fernandez.foro_hub.domain.topico.Topico;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {
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
}