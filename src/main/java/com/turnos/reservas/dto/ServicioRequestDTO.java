package com.turnos.reservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServicioRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    private String descripcion;

    @Positive(message = "La duracion debe ser mayor a 0")
    private Integer duracionMinutos;

}
