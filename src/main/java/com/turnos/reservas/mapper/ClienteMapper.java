package com.turnos.reservas.mapper;

import org.springframework.stereotype.Component;

import com.turnos.reservas.dto.ClienteRequestDTO;
import com.turnos.reservas.dto.ClienteResponseDTO;
import com.turnos.reservas.entity.Cliente;
import com.turnos.reservas.entity.Usuario;

@Component
public class ClienteMapper {

    // CLIENTE A CLIENTERESPONSEDTO
    public ClienteResponseDTO clienteToResponse(Cliente clien) {

        ClienteResponseDTO cliente = new ClienteResponseDTO();
        cliente.setId(clien.getId());
        cliente.setNombre(clien.getNombre());
        cliente.setApellido(clien.getApellido());
        cliente.setEmail(clien.getEmail());
        cliente.setTelefono(clien.getTelefono());

        return cliente;
    }

    // CLIENTEREQUESTDTO A CLIENTE
    public Cliente requestToCliente(ClienteRequestDTO clienteRequestDTO, Usuario usu) {

        Cliente cliente = new Cliente();

        cliente.setNombre(clienteRequestDTO.getNombre());
        cliente.setApellido(clienteRequestDTO.getApellido());
        cliente.setEmail(clienteRequestDTO.getEmail());
        cliente.setTelefono(clienteRequestDTO.getTelefono());
        cliente.setUsuario(usu);

        return cliente;
    }

}
