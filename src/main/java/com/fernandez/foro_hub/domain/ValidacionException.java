package com.fernandez.foro_hub.domain;

public class ValidacionException extends RuntimeException {//Extiende de RuntimeException, en lugar de throwable. Así es más específico

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
