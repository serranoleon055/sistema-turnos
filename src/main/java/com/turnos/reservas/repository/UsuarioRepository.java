package com.turnos.reservas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.turnos.reservas.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.cliente")
    List<Usuario> findAllWithCliente();

}
