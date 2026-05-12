package com.turnos.reservas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turnos.reservas.dto.ServicioRequestDTO;
import com.turnos.reservas.dto.ServicioResponseDTO;
import com.turnos.reservas.entity.Servicio;
import com.turnos.reservas.excepcion.ResourceNotFoundException;
import com.turnos.reservas.mapper.ServicioMapper;
import com.turnos.reservas.repository.ServicioRepository;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;

    public ServicioService(ServicioRepository servicioRepository, ServicioMapper servicioMapper) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
    }

    // GET
    public List<ServicioResponseDTO> obtenerTodos() {
        return servicioRepository.findAll().stream()
                .map(servicioMapper::servicioToResponse)
                .collect(Collectors.toList());
    }

    // POST
    public ServicioResponseDTO crearServicio(ServicioRequestDTO servicioRequestDTO) {
        Servicio servicio = servicioMapper.requestToServicio(servicioRequestDTO);
        servicioRepository.save(servicio);

        ServicioResponseDTO respuesta = servicioMapper.servicioToResponse(servicio);
        return respuesta;
    }

    // PUT
    public ServicioResponseDTO actualizar(Long id, ServicioRequestDTO servicioRequestDTO) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));

        servicio.setNombre(servicioRequestDTO.getNombre());
        servicio.setDescripcion(servicioRequestDTO.getDescripcion());
        servicio.setDuracionMinutos(servicioRequestDTO.getDuracionMinutos());
        servicioRepository.save(servicio);

        return servicioMapper.servicioToResponse(servicio);
    }

    // DELETE
    public void eliminar(Long id) {
        Servicio servicioOPT = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));
        servicioRepository.delete(servicioOPT);

    }

}
