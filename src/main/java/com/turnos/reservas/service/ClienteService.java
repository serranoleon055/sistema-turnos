package com.turnos.reservas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turnos.reservas.dto.ClienteRequestDTO;
import com.turnos.reservas.dto.ClienteResponseDTO;
import com.turnos.reservas.entity.Cliente;
import com.turnos.reservas.entity.Usuario;
import com.turnos.reservas.excepcion.ResourceNotFoundException;
import com.turnos.reservas.mapper.ClienteMapper;
import com.turnos.reservas.repository.ClienteRepository;
import com.turnos.reservas.repository.UsuarioRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final UsuarioRepository usuarioRepository;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper,
            UsuarioRepository usuarioRepository) {
        this.clienteMapper = clienteMapper;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // GET
    public List<ClienteResponseDTO> obtenerTodos() {

        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::clienteToResponse)
                .collect(Collectors.toList());
    }

    // POST
    public ClienteResponseDTO crearCliente(ClienteRequestDTO clienteRequestDTO) {
        Usuario usu = usuarioRepository.findById(clienteRequestDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro el usuario con id: " + clienteRequestDTO.getUsuarioId()));

        Cliente cliente = clienteMapper.requestToCliente(clienteRequestDTO, usu);
        clienteRepository.save(cliente);
        ClienteResponseDTO respuesta = clienteMapper.clienteToResponse(cliente);
        return respuesta;
    }

    // GET ID
    public ClienteResponseDTO obtenerPorId(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        ClienteResponseDTO respuesta = clienteMapper.clienteToResponse(cliente);
        return respuesta;
    }

    // PUT
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO clienteRequestDTO) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        Usuario usu = usuarioRepository.findById(clienteRequestDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro el usuario con id: " + clienteRequestDTO.getUsuarioId()));

        cliente = clienteMapper.requestToCliente(clienteRequestDTO, usu);
        cliente.setId(id);
        clienteRepository.save(cliente);

        ClienteResponseDTO respuesta = clienteMapper.clienteToResponse(cliente);
        return respuesta;
    }

    // DELETE
    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        clienteRepository.delete(cliente);
    }

}
