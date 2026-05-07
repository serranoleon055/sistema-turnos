package com.turnos.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnos.reservas.entity.Profesional;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {

}
