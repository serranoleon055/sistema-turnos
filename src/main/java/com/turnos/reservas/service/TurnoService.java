package com.turnos.reservas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turnos.reservas.dto.TurnoRequestDTO;
import com.turnos.reservas.dto.TurnoResponseDTO;
import com.turnos.reservas.entity.Cliente;
import com.turnos.reservas.entity.Profesional;
import com.turnos.reservas.entity.Servicio;
import com.turnos.reservas.entity.Turno;
import com.turnos.reservas.entity.Usuario;
import com.turnos.reservas.enums.EstadoTurno;
import com.turnos.reservas.enums.Rol;
import com.turnos.reservas.excepcion.BadRequestException;
import com.turnos.reservas.excepcion.ResourceNotFoundException;
import com.turnos.reservas.mapper.TurnoMapper;
import com.turnos.reservas.repository.ClienteRepository;
import com.turnos.reservas.repository.ProfesionalRepository;
import com.turnos.reservas.repository.ServicioRepository;
import com.turnos.reservas.repository.TurnoRepository;
import com.turnos.reservas.repository.UsuarioRepository;

@Service
public class TurnoService {

        private final TurnoRepository turnoRepository;
        private final TurnoMapper turnoMapper;
        private final ClienteRepository clienteRepository;
        private final ServicioRepository servicioRepository;
        private final ProfesionalRepository profesionalRepository;
        private final UsuarioRepository usuarioRepository;

        public TurnoService(TurnoRepository turnoRepository, TurnoMapper turnoMapper,
                        ClienteRepository clienteRepository,
                        ServicioRepository servicioRepository, ProfesionalRepository profesionalRepository,
                        UsuarioRepository usuarioRepository) {
                this.turnoRepository = turnoRepository;
                this.turnoMapper = turnoMapper;
                this.clienteRepository = clienteRepository;
                this.servicioRepository = servicioRepository;
                this.profesionalRepository = profesionalRepository;
                this.usuarioRepository = usuarioRepository;
        }

        // GET
        public List<TurnoResponseDTO> obtenerTodos(String email) {
                Usuario usuario = usuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

                if (usuario.getRol() == Rol.ADMIN) {
                        return turnoRepository.findAll().stream()
                                        .map(turnoMapper::turnoToResponse)
                                        .collect(Collectors.toList());
                }

                Cliente cliente = clienteRepository.findByUsuario(usuario)
                                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

                return turnoRepository.findByCliente(cliente).stream()
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
        public TurnoResponseDTO crearTurno(TurnoRequestDTO turnoRequestDTO, String email) {

                Usuario usuario = usuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

                Cliente cliente = clienteRepository.findByUsuario(usuario)
                                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

                Servicio servicio = servicioRepository.findById(turnoRequestDTO.getIdServicio())
                                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

                Profesional profesional = profesionalRepository.findById(turnoRequestDTO.getIdProfesional())
                                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado"));

                Turno turno = turnoMapper.requestToTurno(turnoRequestDTO, cliente, servicio, profesional);

                LocalDateTime inicioNuevo = turno.getFechaHora();
                LocalDateTime finNuevo = inicioNuevo.plusMinutes(servicio.getDuracionMinutos());

                List<Turno> turnosExistentes = turnoRepository.findByProfesional(turno.getProfesional());

                boolean solapamiento = turnosExistentes.stream()
                                .filter(t -> t.getEstado() != EstadoTurno.CANCELADO)
                                .anyMatch(t -> {
                                        LocalDateTime inicioExist = t.getFechaHora();
                                        LocalDateTime finExist = inicioExist
                                                        .plusMinutes(t.getServicio().getDuracionMinutos());
                                        return inicioNuevo.isBefore(finExist) && finNuevo.isAfter(inicioExist);
                                });

                if (solapamiento)
                        throw new BadRequestException("El profesional ya tiene un turno en ese horario");

                turnoRepository.save(turno);
                return turnoMapper.turnoToResponse(turno);
        }

        // PUT
        public TurnoResponseDTO cambiarEstado(Long idTurno, EstadoTurno estadoTurno) {

                Turno turno = turnoRepository.findById(idTurno)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Turno no encontrado con id: " + idTurno));

                boolean transicionValida = ((estadoTurno == EstadoTurno.CONFIRMADO
                                && turno.getEstado() == EstadoTurno.PENDIENTE) ||
                                (estadoTurno == EstadoTurno.CANCELADO && turno.getEstado() == EstadoTurno.PENDIENTE)) ||
                                (estadoTurno == EstadoTurno.COMPLETADO && turno.getEstado() == EstadoTurno.CONFIRMADO)
                                ||
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
