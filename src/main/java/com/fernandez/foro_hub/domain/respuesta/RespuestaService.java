package com.fernandez.foro_hub.domain.respuesta;

import com.fernandez.foro_hub.domain.ValidacionException;
import com.fernandez.foro_hub.domain.topico.*;
import com.fernandez.foro_hub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RespuestaService {
    @Autowired private TopicoRepository topicoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RespuestaRepository respuestaRepository;

    @Transactional
    public DatosDetalleRespuesta crear(DatosRegistroRespuesta datos) {

        if (!topicoRepository.existsById(datos.topicoId())) {
            throw new ValidacionException("No existe un curso con el id informado");
        }

        if (datos.autorId() != null && !usuarioRepository.existsById(datos.autorId())) {
            throw new ValidacionException("No existe un usuario con el id informado");
        }
        var topico = topicoRepository.findById(datos.topicoId()).get();
        var usuario = usuarioRepository.findById(datos.autorId()).get();
        var respuesta = new Respuesta(null, datos.mensaje(), topico, LocalDateTime.now(), usuario,  false);
        topico.agregarRespuesta(respuesta);
        usuario.agregarRespuesta(respuesta);
        respuestaRepository.save(respuesta);
        return new DatosDetalleRespuesta(respuesta);
    }

    @Transactional
    public Respuesta actualizar(DatosActualizacionRespuesta datos) {
        if (!respuestaRepository.existsById(datos.id())) {
            throw new ValidacionException("Id de la respuesta informada no existe");
        }
        var respuesta = respuestaRepository.getReferenceById(datos.id());
        respuesta.actualizarInformaciones(datos);
        return respuesta;
    }

    public Respuesta detallar(Long id) {
        if (!respuestaRepository.existsById(id)) {
            throw new ValidacionException("Id de la respuesta informada no existe");
        }
        return respuestaRepository.getReferenceById(id);
    }
}
