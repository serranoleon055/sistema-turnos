package com.turnos.reservas.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.turnos.reservas.dto.AuthRequestDTO;
import com.turnos.reservas.dto.AuthResponseDTO;
import com.turnos.reservas.entity.Usuario;
import com.turnos.reservas.repository.UsuarioRepository;
import com.turnos.reservas.security.JwtService;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO registro(AuthRequestDTO authRequestDTO) {

        Usuario usuario = new Usuario();
        usuario.setEmail(authRequestDTO.getEmail());
        usuario.setContrasena(passwordEncoder.encode(authRequestDTO.getContrasena()));
        usuario.setRol(authRequestDTO.getRol());

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);
        AuthResponseDTO respuesta = new AuthResponseDTO();
        respuesta.setToken(token);

        return respuesta;
    }

    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getContrasena()));

        Usuario usuario = usuarioRepository.findByEmail(authRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("No se encontro el username"));

        String token = jwtService.generateToken(usuario);
        AuthResponseDTO respuesta = new AuthResponseDTO();
        respuesta.setToken(token);
        return respuesta;
    }

}
