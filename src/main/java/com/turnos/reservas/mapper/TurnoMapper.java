package com.turnos.reservas.mapper;

import org.springframework.stereotype.Component;

import com.turnos.reservas.dto.TurnoRequestDTO;
import com.turnos.reservas.dto.TurnoResponseDTO;
import com.turnos.reservas.entity.Cliente;
import com.turnos.reservas.entity.Profesional;
import com.turnos.reservas.entity.Servicio;
import com.turnos.reservas.entity.Turno;
import com.turnos.reservas.enums.EstadoTurno;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class TurnoMapper {

    public Turno requestToTurno(TurnoRequestDTO turnoRequestDTO, Cliente cliente, Servicio servicio,
            Profesional profesional) {
        Turno turno = Turno.builder()
                .fechaHora(turnoRequestDTO.getFechaHora())
                .estado(EstadoTurno.PENDIENTE)
                .cliente(cliente)
                .servicio(servicio)
                .profesional(profesional)
                .build();

        return turno;
    }

    public TurnoResponseDTO turnoToResponse(Turno turno) {

        TurnoResponseDTO respuesta = TurnoResponseDTO.builder()
                .id(turno.getId())
                .idCliente(turno.getCliente().getId())
                .nombreCliente(turno.getCliente().getNombre())
                .idProfesional(turno.getProfesional().getId())
                .nombreProfesional(turno.getProfesional().getNombre())
                .idServicio(turno.getServicio().getId())
                .nombreServicio(turno.getServicio().getNombre())
                .build();
        return respuesta;
    }

}
