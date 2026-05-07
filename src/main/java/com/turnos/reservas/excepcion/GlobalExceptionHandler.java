package com.turnos.reservas.excepcion;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.turnos.reservas.dto.ErrorResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException ex) {
        // construir ErrorResponseDTO con status 404 y ex.getMessage()

        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setStatus(404);
        error.setMensaje(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        // devolver ResponseEntity con status 404

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(BadRequestException ex) {
        // construir ErrorResponseDTO con status 400 y ex.getMessage()
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setStatus(400);
        error.setMensaje(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());

        // devolver ResponseEntity con status 400
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        String mensajes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setStatus(400);
        error.setMensaje(mensajes);
        error.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(error);
    }

}
