package com.turnos.reservas.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.turnos.reservas.enums.EstadoTurno;

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
public class TurnoResponseDTO {

    private Long id;
    private Long idCliente;
    private String nombreCliente;
    private Long idServicio;
    private String nombreServicio;
    private Long idProfesional;
    private String nombreProfesional;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHora;

    private EstadoTurno estado;

}
