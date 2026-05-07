package com.turnos.reservas.dto;

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
public class ServicioResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracionMinutos;

}
