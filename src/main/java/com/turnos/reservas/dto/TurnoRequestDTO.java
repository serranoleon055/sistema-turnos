package com.turnos.reservas.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnoRequestDTO {

    @NotNull(message = "La fecha/hora no debe ser nula")
    @Future(message = "La fecha/hora deben ser en el futuro")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHora;

    @NotNull(message = "El id del profesional no debe ser nulo")
    @Positive(message = "El id del profesional debe ser positivo")
    private Long idProfesional;

    @NotNull(message = "El id del servicio no debe ser nulo")
    @Positive(message = "El id del servicio debe ser positivo")
    private Long idServicio;

}
