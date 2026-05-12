package com.turnos.reservas;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.turnos.reservas.entity.Usuario;
import com.turnos.reservas.enums.Rol;
import com.turnos.reservas.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByEmail("admin@turnosapp.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setEmail("admin@turnosapp.com");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("✅ Admin inicial creado: admin@turnosapp.com / admin123");
        }
    }
}