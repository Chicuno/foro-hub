package com.fernandez.foro_hub.domain.topico;

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
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public DatosListaTopico crear(DatosCreacionTopico datos) {

        if (!cursoRepository.existsById(datos.idCurso())) {
            throw new ValidacionException("No existe un curso con el id informado");
        }

        if (datos.idUsuario() != null && !usuarioRepository.existsById(datos.idUsuario())) {
            throw new ValidacionException("No existe un usuario con el id informado");
        }
        var curso = cursoRepository.findById(datos.idCurso()).get();
        var usuario = usuarioRepository.findById(datos.idUsuario()).get();
        var topico = new Topico(null, datos.titulo(), datos.mensaje(), LocalDateTime.now(), Status.SIN_RESPUESTA, usuario, curso, null, true);
        usuario.agregarTopico(topico);
        curso.agregarTopico(topico);
        topicoRepository.save(topico);
        return new DatosListaTopico(topico);
    }

    @Transactional
    public DatosDetalleTopico actualizar(DatosActualizacionTopico datos) {
        var topico = topicoRepository.findByIdAndActivoTrue(datos.id())
                .orElseThrow(() -> new ValidacionException("Id del topico informado no existe"));
        topico.actualizarInformaciones(datos);
        return new DatosDetalleTopico(topico);
    }

    @Transactional
    public void eliminar(Long id) {
        var topico = topicoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ValidacionException("Id del topico informado no existe"));
        topico.eliminar();
    }

    public Page<DatosListaTopico> listar(Pageable paginacion) {
        return topicoRepository.findByActivoTrue(paginacion)
                .map(DatosListaTopico::new);
    }

    public Page<DatosListaTopico> listarPorStatus(Status status, Pageable paginacion) {
        return topicoRepository.findByStatusAndActivoTrue(status, paginacion)
                .map(DatosListaTopico::new);
    }

    @Transactional(readOnly = true)
    public DatosDetalleTopico detallar(Long id) {
        var topico = topicoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ValidacionException("Id del topico informado no existe"));
        return new DatosDetalleTopico(topico);
    }
}
