package com.turnos.reservas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turnos.reservas.dto.UsuarioRequestDTO;
import com.turnos.reservas.dto.UsuarioResponseDTO;
import com.turnos.reservas.entity.Usuario;
import com.turnos.reservas.excepcion.ResourceNotFoundException;
import com.turnos.reservas.mapper.UsuarioMapper;
import com.turnos.reservas.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
        this.usuarioRepository = usuarioRepository;
    }

    // GET
    public List<UsuarioResponseDTO> obtenerTodos() {

        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::usuarioToResponse)
                .collect(Collectors.toList());
    }

    // POST
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioRequestDTO) {

        Usuario usu = usuarioMapper.requestToUsuario(usuarioRequestDTO);
        usuarioRepository.save(usu);

        UsuarioResponseDTO respuesta = usuarioMapper.usuarioToResponse(usu);
        return respuesta;
    }

    // GET ID
    public UsuarioResponseDTO obtenerPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        UsuarioResponseDTO respuesta = usuarioMapper.usuarioToResponse(usuario);
        return respuesta;
    }

}
