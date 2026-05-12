package com.turnos.reservas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turnos.reservas.dto.ProfesionalRequestDTO;
import com.turnos.reservas.dto.ProfesionalResponseDTO;
import com.turnos.reservas.entity.Profesional;
import com.turnos.reservas.entity.Servicio;
import com.turnos.reservas.excepcion.ResourceNotFoundException;
import com.turnos.reservas.mapper.ProfesionalMapper;
import com.turnos.reservas.repository.ProfesionalRepository;
import com.turnos.reservas.repository.ServicioRepository;

@Service
public class ProfesionalService {

    private final ProfesionalRepository profesionalRepository;
    private final ProfesionalMapper profesionalMapper;
    private final ServicioRepository servicioRepository;

    public ProfesionalService(ProfesionalRepository profesionalRepository, ProfesionalMapper profesionalMapper,
            ServicioRepository servicioRepository) {
        this.profesionalMapper = profesionalMapper;
        this.profesionalRepository = profesionalRepository;
        this.servicioRepository = servicioRepository;
    }

    // GET
    public List<ProfesionalResponseDTO> obtenerTodos() {

        return profesionalRepository.findAll()
                .stream()
                .map(profesionalMapper::profesionalToResponse)
                .collect(Collectors.toList());
    }

    // POST
    public ProfesionalResponseDTO crearProfesional(ProfesionalRequestDTO profesionalRequestDTO) {

        Profesional profesional = profesionalMapper.requestToProfesional(profesionalRequestDTO);
        profesionalRepository.save(profesional);

        ProfesionalResponseDTO respuesta = profesionalMapper.profesionalToResponse(profesional);
        return respuesta;
    }

    // GET ID
    public ProfesionalResponseDTO obtenerPorId(Long id) {

        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con id:" + id));

        ProfesionalResponseDTO respuesta = profesionalMapper.profesionalToResponse(profesional);
        return respuesta;

    }

    // PUT
    public ProfesionalResponseDTO actualizar(Long id, ProfesionalRequestDTO profesionalRequestDTO) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con id:" + id));

        profesional.setNombre(profesionalRequestDTO.getNombre());
        profesional.setApellido(profesionalRequestDTO.getApellido());
        profesional.setEspecialidad(profesionalRequestDTO.getEspecialidad());

        if (profesionalRequestDTO.getServicioIds() != null) {
            List<Servicio> servicios = servicioRepository.findAllById(profesionalRequestDTO.getServicioIds());
            profesional.setServicios(servicios);
        }

        profesionalRepository.save(profesional);
        return profesionalMapper.profesionalToResponse(profesional);
    }

    // DELETE
    public void eliminar(Long id) {
        Profesional prof = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con id:" + id));
        profesionalRepository.delete(prof);
    }

}
