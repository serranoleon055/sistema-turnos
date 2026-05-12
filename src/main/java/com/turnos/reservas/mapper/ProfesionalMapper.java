package com.turnos.reservas.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.turnos.reservas.dto.ProfesionalRequestDTO;
import com.turnos.reservas.dto.ProfesionalResponseDTO;
import com.turnos.reservas.entity.Profesional;
import com.turnos.reservas.entity.Servicio;
import com.turnos.reservas.repository.ServicioRepository;

@Component
public class ProfesionalMapper {

    private final ServicioRepository servicioRepository;

    public ProfesionalMapper(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public ProfesionalResponseDTO profesionalToResponse(Profesional profesional) {
        ProfesionalResponseDTO profesionalDTO = new ProfesionalResponseDTO();
        profesionalDTO.setId(profesional.getId());
        profesionalDTO.setNombre(profesional.getNombre());
        profesionalDTO.setApellido(profesional.getApellido());
        profesionalDTO.setEspecialidad(profesional.getEspecialidad());
        if (profesional.getServicios() != null) {
            profesionalDTO.setServicioIds(
                    profesional.getServicios().stream()
                            .map(Servicio::getId)
                            .collect(Collectors.toList()));
        }
        return profesionalDTO;
    }

    public Profesional requestToProfesional(ProfesionalRequestDTO profesionalRequestDTO) {
        Profesional profesional = new Profesional();
        profesional.setNombre(profesionalRequestDTO.getNombre());
        profesional.setApellido(profesionalRequestDTO.getApellido());
        profesional.setEspecialidad(profesionalRequestDTO.getEspecialidad());

        if (profesionalRequestDTO.getServicioIds() != null && !profesionalRequestDTO.getServicioIds().isEmpty()) {
            List<Servicio> servicios = servicioRepository.findAllById(profesionalRequestDTO.getServicioIds());
            profesional.setServicios(servicios);
        } else {
            profesional.setServicios(new ArrayList<>());
        }
        return profesional;
    }

}
