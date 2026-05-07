package com.turnos.reservas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turnos.reservas.dto.TurnoRequestDTO;
import com.turnos.reservas.dto.TurnoResponseDTO;
import com.turnos.reservas.entity.Cliente;
import com.turnos.reservas.entity.Profesional;
import com.turnos.reservas.entity.Servicio;
import com.turnos.reservas.entity.Turno;
import com.turnos.reservas.enums.EstadoTurno;
import com.turnos.reservas.excepcion.BadRequestException;
import com.turnos.reservas.excepcion.ResourceNotFoundException;
import com.turnos.reservas.mapper.TurnoMapper;
import com.turnos.reservas.repository.ClienteRepository;
import com.turnos.reservas.repository.ProfesionalRepository;
import com.turnos.reservas.repository.ServicioRepository;
import com.turnos.reservas.repository.TurnoRepository;

@Service
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final TurnoMapper turnoMapper;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;
    private final ProfesionalRepository profesionalRepository;

    public TurnoService(TurnoRepository turnoRepository, TurnoMapper turnoMapper, ClienteRepository clienteRepository,
            ServicioRepository servicioRepository, ProfesionalRepository profesionalRepository) {
        this.turnoRepository = turnoRepository;
        this.turnoMapper = turnoMapper;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
        this.profesionalRepository = profesionalRepository;
    }

    // GET
    public List<TurnoResponseDTO> obtenerTodos() {
        return turnoRepository.findAll().stream()
                .map(turnoMapper::turnoToResponse)
                .collect(Collectors.toList());
    }

    public TurnoResponseDTO obtenerPorId(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + id));

        TurnoResponseDTO respuesta = turnoMapper.turnoToResponse(turno);
        return respuesta;
    }

    // POST
    public TurnoResponseDTO crearTurno(TurnoRequestDTO turnoRequestDTO) {

        Cliente cliente = clienteRepository.findById(turnoRequestDTO.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con id: " + turnoRequestDTO.getIdCliente()));

        Servicio servicio = servicioRepository.findById(turnoRequestDTO.getIdServicio())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Servicio no encontrado con id: " + turnoRequestDTO.getIdServicio()));

        Profesional profesional = profesionalRepository.findById(turnoRequestDTO.getIdProfesional())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profesional no encontrado con id: " + turnoRequestDTO.getIdProfesional()));

        Turno turno = turnoMapper.requestToTurno(turnoRequestDTO, cliente, servicio, profesional);

        boolean ocupado = turnoRepository.existsByProfesionalAndFechaHora(turno.getProfesional(), turno.getFechaHora());
        if (ocupado) {
            throw new BadRequestException("El turno ya esta ocupado");
        }

        turnoRepository.save(turno);

        TurnoResponseDTO respuesta = turnoMapper.turnoToResponse(turno);
        return respuesta;
    }

    // PUT
    public TurnoResponseDTO cambiarEstado(Long idTurno, EstadoTurno estadoTurno) {

        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + idTurno));

        boolean transicionValida = ((estadoTurno == EstadoTurno.CONFIRMADO
                && turno.getEstado() == EstadoTurno.PENDIENTE) ||
                (estadoTurno == EstadoTurno.CANCELADO && turno.getEstado() == EstadoTurno.PENDIENTE)) ||
                (estadoTurno == EstadoTurno.COMPLETADO && turno.getEstado() == EstadoTurno.CONFIRMADO) ||
                (estadoTurno == EstadoTurno.CANCELADO && turno.getEstado() == EstadoTurno.CONFIRMADO);

        if (!transicionValida) {
            throw new BadRequestException("Debe introducir una transicion valida");
        }

        turno.setEstado(estadoTurno);
        turnoRepository.save(turno);

        return turnoMapper.turnoToResponse(turno);
    }

    // DELETE
    public void eliminar(Long id) {

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + id));
        turnoRepository.delete(turno);
    }

}
