package com.turnos.reservas.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionalResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String especialidad;
    private List<Long> servicioIds;

}
