package com.turnos.reservas.entity;

import java.time.LocalDateTime;

import com.turnos.reservas.enums.EstadoTurno;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    private EstadoTurno estado;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Profesional profesional;

    @ManyToOne
    private Servicio servicio;

}
