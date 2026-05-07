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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turnos.reservas.dto.TurnoRequestDTO;
import com.turnos.reservas.dto.TurnoResponseDTO;
import com.turnos.reservas.enums.EstadoTurno;
import com.turnos.reservas.service.TurnoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    // GET
    @GetMapping
    public ResponseEntity<List<TurnoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(turnoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> obtenerPorId(@PathVariable Long id) {
        TurnoResponseDTO turno = turnoService.obtenerPorId(id);
        return ResponseEntity.ok(turno);
    }

    @PostMapping
    public ResponseEntity<TurnoResponseDTO> crearTurno(@Valid @RequestBody TurnoRequestDTO turnoRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(turnoService.crearTurno(turnoRequestDTO));
    }

    // PUT
    @PutMapping("/{id}/estado")
    public ResponseEntity<TurnoResponseDTO> cambiarEstado(@PathVariable Long id,
            @RequestParam EstadoTurno estadoTurno) {

        TurnoResponseDTO turno = turnoService.cambiarEstado(id, estadoTurno);
        return ResponseEntity.ok(turno);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        turnoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
