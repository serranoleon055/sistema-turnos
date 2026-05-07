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

import com.turnos.reservas.dto.ProfesionalRequestDTO;
import com.turnos.reservas.dto.ProfesionalResponseDTO;
import com.turnos.reservas.service.ProfesionalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    public ProfesionalController(ProfesionalService profesionalService) {
        this.profesionalService = profesionalService;
    }

    // GET
    @GetMapping
    public ResponseEntity<List<ProfesionalResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(profesionalService.obtenerTodos());
    }

    // POST
    @PostMapping
    public ResponseEntity<ProfesionalResponseDTO> crearProfesional(
            @Valid @RequestBody ProfesionalRequestDTO profesionalRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profesionalService.crearProfesional(profesionalRequestDTO));
    }

    // GET ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalResponseDTO> obtenerPorId(@PathVariable Long id) {

        ProfesionalResponseDTO profesional = profesionalService.obtenerPorId(id);
        return ResponseEntity.ok(profesional);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody ProfesionalRequestDTO profesionalRequestDTO) {

        ProfesionalResponseDTO profesional = profesionalService.actualizar(id, profesionalRequestDTO);
        return ResponseEntity.ok(profesional);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        profesionalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
