package com.turnos.reservas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.turnos.reservas.entity.Cliente;
import com.turnos.reservas.entity.Profesional;
import com.turnos.reservas.entity.Turno;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

        @Query("SELECT COUNT(t) > 0 FROM Turno t " +
                        "WHERE t.profesional = :profesional " +
                        "AND t.fechaHora < :fin " +
                        "AND FUNCTION('ADDTIME', t.fechaHora, FUNCTION('SEC_TO_TIME', :duracion * 60)) > :inicio")
        boolean existeSolapamiento(
                        @Param("profesional") Profesional profesional,
                        @Param("inicio") LocalDateTime inicio,
                        @Param("fin") LocalDateTime fin,
                        @Param("duracion") int duracion);

        public List<Turno> findByProfesionalAndFechaHora(Profesional profesional, LocalDateTime fechaHora);

        public List<Turno> findByCliente(Cliente cliente);

        @Modifying
        @Query(value = "DELETE FROM turno WHERE cliente_id = :clienteId", nativeQuery = true)
        void deleteByClienteIdNative(@Param("clienteId") Long clienteId);

        List<Turno> findByProfesionalAndFechaHoraBetween(Profesional profesional, LocalDateTime desde,
                        LocalDateTime hasta);

        List<Turno> findByProfesional(Profesional profesional);

}
