package com.turnos.reservas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turnos.reservas.dto.ClienteRequestDTO;
import com.turnos.reservas.dto.ClienteResponseDTO;
import com.turnos.reservas.entity.Cliente;
import com.turnos.reservas.entity.Usuario;
import com.turnos.reservas.enums.Rol;
import com.turnos.reservas.excepcion.ResourceNotFoundException;
import com.turnos.reservas.mapper.ClienteMapper;
import com.turnos.reservas.repository.ClienteRepository;
import com.turnos.reservas.repository.TurnoRepository;
import com.turnos.reservas.repository.UsuarioRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final UsuarioRepository usuarioRepository;
    private final TurnoRepository turnoRepository;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper,
            UsuarioRepository usuarioRepository, TurnoRepository turnoRepository) {
        this.clienteMapper = clienteMapper;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.turnoRepository = turnoRepository;
    }

    // GET
    public List<ClienteResponseDTO> obtenerTodos() {

        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::clienteToResponse)
                .collect(Collectors.toList());
    }

    // GET ID
    public ClienteResponseDTO obtenerPorId(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        ClienteResponseDTO respuesta = clienteMapper.clienteToResponse(cliente);
        return respuesta;
    }

    // POST
    public ClienteResponseDTO crearCliente(ClienteRequestDTO clienteRequestDTO, String email) {
        Usuario usu = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Cliente cliente = clienteMapper.requestToCliente(clienteRequestDTO, usu);
        clienteRepository.save(cliente);
        return clienteMapper.clienteToResponse(cliente);
    }

    // PUT
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO clienteRequestDTO, String email) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        Usuario usu = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (usu.getRol() == Rol.ADMIN) {
            cliente = clienteMapper.putToCliente(clienteRequestDTO, cliente.getUsuario(), cliente);
        } else {
            cliente = clienteMapper.putToCliente(clienteRequestDTO, usu, cliente);
        }
        cliente.setId(id);
        clienteRepository.save(cliente);

        ClienteResponseDTO respuesta = clienteMapper.clienteToResponse(cliente);
        return respuesta;
    }

    // DELETE
    @Transactional
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        turnoRepository.deleteByClienteIdNative(id);
        clienteRepository.deleteByIdNative(id);
    }

}
