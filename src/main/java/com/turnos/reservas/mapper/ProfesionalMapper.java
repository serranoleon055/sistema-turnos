package com.turnos.reservas.mapper;

import org.springframework.stereotype.Component;

import com.turnos.reservas.dto.ProfesionalRequestDTO;
import com.turnos.reservas.dto.ProfesionalResponseDTO;
import com.turnos.reservas.entity.Profesional;

@Component
public class ProfesionalMapper {

    public ProfesionalResponseDTO profesionalToResponse(Profesional profesional) {

        ProfesionalResponseDTO profesionalDTO = new ProfesionalResponseDTO();
        profesionalDTO.setId(profesional.getId());
        profesionalDTO.setNombre(profesional.getNombre());
        profesionalDTO.setApellido(profesional.getApellido());
        profesionalDTO.setEspecialidad(profesional.getEspecialidad());

        return profesionalDTO;
    }

    public Profesional requestToProfesional(ProfesionalRequestDTO profesionalRequestDTO) {

        Profesional profesional = new Profesional();
        profesional.setNombre(profesionalRequestDTO.getNombre());
        profesional.setApellido(profesionalRequestDTO.getApellido());
        profesional.setEspecialidad(profesionalRequestDTO.getEspecialidad());

        return profesional;
    }

}
