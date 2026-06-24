package com.fernandez.foro_hub.domain.pregunta;

import com.fernandez.foro_hub.domain.ValidacionException;
import com.fernandez.foro_hub.domain.curso.CursoRepository;
import com.fernandez.foro_hub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public DatosListaPregunta crear(DatosCreacionPregunta datos) {

        if (!cursoRepository.existsById(datos.idCurso())) {
            throw new ValidacionException("No existe un curso con el id informado");
        }

        if (datos.idUsuario() != null && !usuarioRepository.existsById(datos.idUsuario())) {
            throw new ValidacionException("No existe un usuario con el id informado");
        }
        var curso = cursoRepository.findById(datos.idCurso()).get();
        var usuario = usuarioRepository.findById(datos.idUsuario()).get();
        var pregunta = new Pregunta(null, datos.titulo(), datos.mensaje(), LocalDateTime.now(), Status.SIN_RESPUESTA, usuario, curso, null, true);
        usuario.agregarPregunta(pregunta);
        curso.agregarPregunta(pregunta);
        preguntaRepository.save(pregunta);
        return new DatosListaPregunta(pregunta);
    }

    @Transactional
    public DatosDetallePregunta actualizar(DatosActualizacionPregunta datos) {
        var pregunta = preguntaRepository.findByIdAndActivoTrue(datos.id())
                .orElseThrow(() -> new ValidacionException("Id del pregunta informado no existe"));
        pregunta.actualizarInformaciones(datos);
        return new DatosDetallePregunta(pregunta);
    }

    @Transactional
    public void eliminar(Long id) {
        var pregunta = preguntaRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ValidacionException("Id del pregunta informado no existe"));
        pregunta.eliminar();
    }

    public Page<DatosListaPregunta> listar(Pageable paginacion) {
        return preguntaRepository.findByActivoTrue(paginacion)
                .map(DatosListaPregunta::new);
    }

    public Page<DatosListaPregunta> listarPorStatus(Status status, Pageable paginacion) {
        return preguntaRepository.findByStatusAndActivoTrue(status, paginacion)
                .map(DatosListaPregunta::new);
    }

    @Transactional(readOnly = true)
    public DatosDetallePregunta detallar(Long id) {
        var pregunta = preguntaRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ValidacionException("Id del pregunta informado no existe"));
        return new DatosDetallePregunta(pregunta);
    }
}
