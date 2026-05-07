package com.turnos.reservas;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
import com.turnos.reservas.enums.EstadoTurno;
import com.turnos.reservas.excepcion.BadRequestException;
import com.turnos.reservas.excepcion.ResourceNotFoundException;
import com.turnos.reservas.mapper.TurnoMapper;
import com.turnos.reservas.repository.ClienteRepository;
import com.turnos.reservas.repository.ProfesionalRepository;
import com.turnos.reservas.repository.ServicioRepository;
import com.turnos.reservas.repository.TurnoRepository;
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

    @InjectMocks
    private TurnoService turnoService;

    @Test
    public void crearTurno_Exitoso() {

        // ARRANGE — preparás todo lo que el método necesita para ejecutarse

        // Creás los objetos que van a "existir" en la base de datos
        Long idCliente = 1L;
        Long idProfesional = 1L;
        Long idServicio = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);

        Profesional profesional = new Profesional();
        profesional.setId(idProfesional);

        Servicio servicio = new Servicio();
        servicio.setId(idServicio);

        // Creás el Turno que el mapper va a devolver
        Turno turno = new Turno();
        turno.setCliente(cliente);
        turno.setProfesional(profesional);
        turno.setServicio(servicio);
        turno.setFechaHora(LocalDateTime.now().plusDays(1));

        // Creás el request que va a recibir el service
        TurnoRequestDTO turnoRequestDTO = new TurnoRequestDTO();
        turnoRequestDTO.setIdCliente(idCliente);
        turnoRequestDTO.setIdProfesional(idProfesional);
        turnoRequestDTO.setIdServicio(idServicio);
        turnoRequestDTO.setFechaHora(LocalDateTime.now().plusDays(1));

        // Configurás los mocks — le decís a cada repository qué devolver
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(profesionalRepository.findById(idProfesional)).thenReturn(Optional.of(profesional));
        when(servicioRepository.findById(idServicio)).thenReturn(Optional.of(servicio));
        when(turnoRepository.existsByProfesionalAndFechaHora(any(), any())).thenReturn(false);
        when(turnoMapper.requestToTurno(any(), any(), any(), any())).thenReturn(turno);

        // ACT — ejecutás el método que estás testeando
        turnoService.crearTurno(turnoRequestDTO);

        // ASSERT — verificás que save fue llamado exactamente una vez
        verify(turnoRepository, times(1)).save(any());
    }

    @Test
    public void crearTurno_ProfOcupado() {

        Long idCliente = 1L;
        Long idProfesional = 1L;
        Long idServicio = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);

        Profesional profesional = new Profesional();
        profesional.setId(idProfesional);

        Servicio servicio = new Servicio();
        servicio.setId(idServicio);

        Turno turno = new Turno();
        turno.setCliente(cliente);
        turno.setProfesional(profesional);
        turno.setServicio(servicio);
        turno.setFechaHora(LocalDateTime.now().plusDays(1));

        TurnoRequestDTO turnoRequestDTO = new TurnoRequestDTO();
        turnoRequestDTO.setIdCliente(idCliente);
        turnoRequestDTO.setIdProfesional(idProfesional);
        turnoRequestDTO.setIdServicio(idServicio);
        turnoRequestDTO.setFechaHora(LocalDateTime.now().plusDays(1));

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(profesionalRepository.findById(idProfesional)).thenReturn(Optional.of(profesional));
        when(servicioRepository.findById(idServicio)).thenReturn(Optional.of(servicio));
        when(turnoRepository.existsByProfesionalAndFechaHora(any(), any())).thenReturn(true);
        when(turnoMapper.requestToTurno(any(), any(), any(), any())).thenReturn(turno);

        assertThrows(BadRequestException.class, () -> turnoService.crearTurno(turnoRequestDTO));
    }

    @Test
    public void crearTurno_ClienteNoExiste() {

        Long idCliente = 1L;
        Long idProfesional = 1L;
        Long idServicio = 1L;

        Profesional profesional = new Profesional();
        profesional.setId(idProfesional);

        Servicio servicio = new Servicio();
        servicio.setId(idServicio);

        Turno turno = new Turno();
        turno.setProfesional(profesional);
        turno.setServicio(servicio);
        turno.setFechaHora(LocalDateTime.now().plusDays(1));

        TurnoRequestDTO turnoRequestDTO = new TurnoRequestDTO();
        turnoRequestDTO.setIdCliente(idCliente);
        turnoRequestDTO.setIdProfesional(idProfesional);
        turnoRequestDTO.setIdServicio(idServicio);
        turnoRequestDTO.setFechaHora(LocalDateTime.now().plusDays(1));

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> turnoService.crearTurno(turnoRequestDTO));
    }

    @Test
    public void cambiarEstado_Valido() {

        Long idTurno = 1L;

        Turno turno = new Turno();
        turno.setId(idTurno);
        turno.setEstado(EstadoTurno.PENDIENTE);

        TurnoResponseDTO turnoResponseDTO = new TurnoResponseDTO();
        turnoResponseDTO.setId(idTurno);

        when(turnoRepository.findById(idTurno)).thenReturn(Optional.of(turno));
        when(turnoMapper.turnoToResponse(any())).thenReturn(turnoResponseDTO);

        turnoService.cambiarEstado(idTurno, EstadoTurno.CONFIRMADO);

        verify(turnoRepository, times(1)).save(any());
    }

    @Test
    public void cambiarEstado_Invalido() {

        Long idTurno = 1L;

        Turno turno = new Turno();
        turno.setId(idTurno);
        turno.setEstado(EstadoTurno.CONFIRMADO);

        when(turnoRepository.findById(idTurno)).thenReturn(Optional.of(turno));

        assertThrows(BadRequestException.class, () -> turnoService.cambiarEstado(1L, EstadoTurno.PENDIENTE));

    }

}
