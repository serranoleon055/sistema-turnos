package com.turnos.reservas.mapper;

import org.springframework.stereotype.Component;

import com.turnos.reservas.dto.ServicioRequestDTO;
import com.turnos.reservas.dto.ServicioResponseDTO;
import com.turnos.reservas.entity.Servicio;

@Component
public class ServicioMapper {

    public ServicioResponseDTO servicioToResponse(Servicio servicio) {
        ServicioResponseDTO respuesta = ServicioResponseDTO.builder()
                .id(servicio.getId())
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .duracionMinutos(servicio.getDuracionMinutos())
                .build();
        return respuesta;
    }

    public Servicio requestToServicio(ServicioRequestDTO servicioRequestDTO) {
        Servicio servicio = Servicio.builder()
                .nombre(servicioRequestDTO.getNombre())
                .descripcion(servicioRequestDTO.getDescripcion())
                .duracionMinutos(servicioRequestDTO.getDuracionMinutos())
                .build();
        return servicio;
    }

}
