package com.turnos.reservas.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.turnos.reservas.dto.UsuarioRequestDTO;
import com.turnos.reservas.dto.UsuarioResponseDTO;
import com.turnos.reservas.entity.Usuario;

@Component
public class UsuarioMapper {

    private final PasswordEncoder passwordEncoder;

    public UsuarioMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO usuarioToResponse(Usuario usu) {
        UsuarioResponseDTO respuesta = new UsuarioResponseDTO();
        respuesta.setId(usu.getId());
        respuesta.setEmail(usu.getEmail());
        respuesta.setRol(usu.getRol());

        return respuesta;
    }

    public Usuario requestToUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usu = new Usuario();
        usu.setEmail(usuarioRequestDTO.getEmail());
        usu.setContrasena(passwordEncoder.encode(usuarioRequestDTO.getContrasena()));
        usu.setRol(usuarioRequestDTO.getRol());

        return usu;
    }

}
