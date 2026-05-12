package com.turnos.reservas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.turnos.reservas.entity.Cliente;
import com.turnos.reservas.entity.Usuario;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    public Optional<Cliente> findByUsuario(Usuario usuario);

    @Modifying
    @Query(value = "DELETE FROM cliente WHERE id = :id", nativeQuery = true)
    void deleteByIdNative(@Param("id") Long id);

}
