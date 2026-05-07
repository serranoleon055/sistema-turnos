package com.turnos.reservas.dto;

import com.turnos.reservas.enums.Rol;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {

    @Email(message = "El email debe tener formatil mail")
    private String email;

    @NotBlank(message = "La contrasena no puede estar vacio")
    private String contrasena;

    @NotNull(message = "El rol no puede ser nulo")
    private Rol rol;

}
