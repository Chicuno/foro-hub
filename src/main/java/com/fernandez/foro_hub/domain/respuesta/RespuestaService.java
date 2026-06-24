package com.fernandez.foro_hub.domain.respuesta;

import com.fernandez.foro_hub.domain.ValidacionException;
import com.fernandez.foro_hub.domain.pregunta.*;
import com.fernandez.foro_hub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RespuestaService {
    @Autowired private PreguntaRepository preguntaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RespuestaRepository respuestaRepository;

    @Transactional
    public DatosDetalleRespuesta crear(DatosRegistroRespuesta datos) {

        if (!preguntaRepository.existsById(datos.preguntaId())) {
            throw new ValidacionException("No existe un curso con el id informado");
        }

        if (datos.autorId() != null && !usuarioRepository.existsById(datos.autorId())) {
            throw new ValidacionException("No existe un usuario con el id informado");
        }
        var pregunta = preguntaRepository.findById(datos.preguntaId()).get();
        var usuario = usuarioRepository.findById(datos.autorId()).get();
        var respuesta = new Respuesta(null, datos.mensaje(), pregunta, LocalDateTime.now(), usuario,  false);
        pregunta.agregarRespuesta(respuesta);
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

    @Transactional(readOnly = true)
    public DatosDetalleRespuesta detallar(Long id) {
        if (!respuestaRepository.existsById(id)) {
            throw new ValidacionException("Id de la respuesta informada no existe");
        }
        var respuesta = respuestaRepository.getReferenceById(id);
        return new DatosDetalleRespuesta(respuesta);
    }
}
