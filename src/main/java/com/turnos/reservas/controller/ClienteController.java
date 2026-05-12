package com.turnos.reservas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turnos.reservas.dto.ClienteRequestDTO;
import com.turnos.reservas.dto.ClienteResponseDTO;
import com.turnos.reservas.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        ClienteResponseDTO clienteResponseDTO = clienteService.crearCliente(clienteRequestDTO,
                userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO clienteRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {

        ClienteResponseDTO cliente = clienteService.actualizar(id, clienteRequestDTO, userDetails.getUsername());
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
