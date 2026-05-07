package com.turnos.reservas.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String especialidad;

    @ManyToMany
    @JoinTable(name = "profesional_servicio", joinColumns = @JoinColumn(name = "profesional_id"), inverseJoinColumns = @JoinColumn(name = "servicio_id"))
    private List<Servicio> servicios;

    @OneToMany(mappedBy = "profesional", fetch = FetchType.LAZY)
    private List<Turno> turnos;

}
