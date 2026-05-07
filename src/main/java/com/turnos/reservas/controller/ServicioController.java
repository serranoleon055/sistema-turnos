package com.turnos.reservas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turnos.reservas.dto.ServicioRequestDTO;
import com.turnos.reservas.dto.ServicioResponseDTO;
import com.turnos.reservas.service.ServicioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    // GET
    @GetMapping
    public ResponseEntity<List<ServicioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(servicioService.obtenerTodos());
    }

    // POST
    @PostMapping
    public ResponseEntity<ServicioResponseDTO> crearServicio(
            @Valid @RequestBody ServicioRequestDTO servicioRequestDTO) {
        ServicioResponseDTO servicioDTO = servicioService.crearServicio(servicioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioDTO);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody ServicioRequestDTO servicioRequestDTO) {
        ServicioResponseDTO servicioDTO = servicioService.actualizar(id, servicioRequestDTO);
        return ResponseEntity.ok(servicioDTO);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
