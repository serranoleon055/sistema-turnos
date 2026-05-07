package com.turnos.reservas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnos.reservas.entity.Profesional;
import com.turnos.reservas.entity.Turno;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    public List<Turno> findByProfesionalAndFechaHora(Profesional profesional, LocalDateTime fechaHora);

    public boolean existsByProfesionalAndFechaHora(Profesional profesional, LocalDateTime fechaHora);

}
