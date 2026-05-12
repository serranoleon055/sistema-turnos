package com.turnos.reservas;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.turnos.reservas.dto.TurnoRequestDTO;
import com.turnos.reservas.dto.TurnoResponseDTO;
import com.turnos.reservas.entity.Cliente;
import com.turnos.reservas.entity.Profesional;
import com.turnos.reservas.entity.Servicio;
import com.turnos.reservas.entity.Turno;
import com.turnos.reservas.entity.Usuario;
import com.turnos.reservas.enums.EstadoTurno;
import com.turnos.reservas.excepcion.BadRequestException;
import com.turnos.reservas.excepcion.ResourceNotFoundException;
import com.turnos.reservas.mapper.TurnoMapper;
import com.turnos.reservas.repository.ClienteRepository;
import com.turnos.reservas.repository.ProfesionalRepository;
import com.turnos.reservas.repository.ServicioRepository;
import com.turnos.reservas.repository.TurnoRepository;
import com.turnos.reservas.repository.UsuarioRepository;
import com.turnos.reservas.service.TurnoService;

@ExtendWith(MockitoExtension.class)
public class TurnoServiceTest {

    @Mock
    private TurnoRepository turnoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ProfesionalRepository profesionalRepository;
    @Mock
    private ServicioRepository servicioRepository;
    @Mock
    private TurnoMapper turnoMapper;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TurnoService turnoService;

    private final String emailTest = "test@email.com";

    @Test
    public void crearTurno_Exitoso() {
        Servicio servicio = new Servicio();
        servicio.setId(1L);
        servicio.setDuracionMinutos(60);

        Profesional profesional = new Profesional();
        profesional.setId(1L);

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail(emailTest);

        LocalDateTime fechaTurno = LocalDateTime.now().plusDays(1);

        Turno turno = new Turno();
        turno.setCliente(cliente);
        turno.setProfesional(profesional);
        turno.setServicio(servicio);
        turno.setFechaHora(fechaTurno);
        turno.setEstado(EstadoTurno.PENDIENTE);

        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setIdProfesional(1L);
        dto.setIdServicio(1L);
        dto.setFechaHora(fechaTurno);

        when(usuarioRepository.findByEmail(emailTest)).thenReturn(Optional.of(usuario));
        when(clienteRepository.findByUsuario(usuario)).thenReturn(Optional.of(cliente));
        when(profesionalRepository.findById(1L)).thenReturn(Optional.of(profesional));
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(turnoRepository.findByProfesional(profesional)).thenReturn(List.of());
        when(turnoMapper.requestToTurno(any(), any(), any(), any())).thenReturn(turno);

        turnoService.crearTurno(dto, emailTest);

        verify(turnoRepository, times(1)).save(any());
    }

    @Test
    public void crearTurno_ProfOcupado() {
        Servicio servicio = new Servicio();
        servicio.setId(1L);
        servicio.setDuracionMinutos(60);

        Profesional profesional = new Profesional();
        profesional.setId(1L);

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setEmail(emailTest);

        // El turno nuevo y el existente solapan: mismo profesional, misma hora
        LocalDateTime fechaTurno = LocalDateTime.now().plusDays(1);

        Turno turnoExistente = new Turno();
        turnoExistente.setProfesional(profesional);
        turnoExistente.setServicio(servicio);
        turnoExistente.setFechaHora(fechaTurno);
        turnoExistente.setEstado(EstadoTurno.PENDIENTE);

        Turno turnoNuevo = new Turno();
        turnoNuevo.setCliente(cliente);
        turnoNuevo.setProfesional(profesional);
        turnoNuevo.setServicio(servicio);
        turnoNuevo.setFechaHora(fechaTurno);
        turnoNuevo.setEstado(EstadoTurno.PENDIENTE);

        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setIdProfesional(1L);
        dto.setIdServicio(1L);
        dto.setFechaHora(fechaTurno);

        when(usuarioRepository.findByEmail(emailTest)).thenReturn(Optional.of(usuario));
        when(clienteRepository.findByUsuario(usuario)).thenReturn(Optional.of(cliente));
        when(profesionalRepository.findById(1L)).thenReturn(Optional.of(profesional));
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(turnoRepository.findByProfesional(profesional)).thenReturn(List.of(turnoExistente));
        when(turnoMapper.requestToTurno(any(), any(), any(), any())).thenReturn(turnoNuevo);

        assertThrows(BadRequestException.class, () -> turnoService.crearTurno(dto, emailTest));
    }

    @Test
    public void crearTurno_ClienteNoExiste() {
        Usuario usuario = new Usuario();
        usuario.setEmail(emailTest);

        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setIdProfesional(1L);
        dto.setIdServicio(1L);
        dto.setFechaHora(LocalDateTime.now().plusDays(1));

        when(usuarioRepository.findByEmail(emailTest)).thenReturn(Optional.of(usuario));
        when(clienteRepository.findByUsuario(usuario)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> turnoService.crearTurno(dto, emailTest));
    }

    @Test
    public void cambiarEstado_Valido() {
        Turno turno = new Turno();
        turno.setId(1L);
        turno.setEstado(EstadoTurno.PENDIENTE);

        TurnoResponseDTO response = new TurnoResponseDTO();
        response.setId(1L);

        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoMapper.turnoToResponse(any())).thenReturn(response);

        turnoService.cambiarEstado(1L, EstadoTurno.CONFIRMADO);

        verify(turnoRepository, times(1)).save(any());
    }

    @Test
    public void cambiarEstado_Invalido() {
        Turno turno = new Turno();
        turno.setId(1L);
        turno.setEstado(EstadoTurno.CONFIRMADO);

        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));

        assertThrows(BadRequestException.class, () -> turnoService.cambiarEstado(1L, EstadoTurno.PENDIENTE));
    }
}