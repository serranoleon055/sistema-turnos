package com.turnos.reservas.dto;

import com.turnos.reservas.enums.Rol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO {

    private String email;
    private String contrasena;
    private Rol rol;

}
