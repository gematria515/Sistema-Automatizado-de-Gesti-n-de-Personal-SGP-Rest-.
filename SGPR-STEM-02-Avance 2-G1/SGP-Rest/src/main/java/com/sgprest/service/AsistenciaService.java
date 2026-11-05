package com.sgprest.service;

import com.sgprest.domain.Asistencia;
import com.sgprest.domain.Turno;
import com.sgprest.repository.AsistenciaRepository;
import com.sgprest.repository.EmpleadoRepository;
import com.sgprest.repository.TurnoRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final TurnoRepository turnoRepository;
    private final EmpleadoRepository empleadoRepository;

    @Value("${sgprest.tolerancia-minutos:5}")
    private long tolerancia;

    @Transactional(readOnly = true)
    public List<Asistencia> getAsistencias(Long idEmpleado) {
        return asistenciaRepository.findByEmpleadoIdOrderByEntradaDesc(idEmpleado);
    }

    // Jornadas registradas entre dos fechas (pre-nómina)
    @Transactional(readOnly = true)
    public List<Asistencia> getAsistencias(LocalDate desde, LocalDate hasta) {
        return asistenciaRepository.findByEntradaBetweenOrderByEntradaAsc(
                desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay());
    }

    @Transactional
    public void marcarEntrada(Long idEmpleado) {
        if (asistenciaRepository
                .findFirstByEmpleadoIdAndSalidaIsNullOrderByEntradaDesc(idEmpleado)
                .isPresent()) {
            throw new IllegalStateException("asistencia.error01");
        }

        LocalDateTime ahora = LocalDateTime.now();

        Optional<Turno> turnoHoy = turnoRepository
                .findFirstByEmpleadoIdAndFecha(idEmpleado, ahora.toLocalDate());

        if (turnoHoy.isEmpty()) {
            throw new IllegalStateException("asistencia.error02");
        }

        Turno turno = turnoHoy.get();

        // Es tardía si llega después de la hora de inicio más la tolerancia
        LocalDateTime limite = turno.getFecha()
                .atTime(turno.getHoraInicio())
                .plusMinutes(tolerancia);

        Asistencia asistencia = new Asistencia();
        asistencia.setEmpleado(empleadoRepository.findById(idEmpleado).get());
        asistencia.setTurno(turno);
        asistencia.setEntrada(ahora);
        asistencia.setTardia(ahora.isAfter(limite));

        asistenciaRepository.save(asistencia);
    }

    @Transactional
    public void iniciarDescanso(Long idEmpleado) {
        Asistencia asistencia = getJornadaAbierta(idEmpleado);

        if (asistencia.getInicioDescanso() != null) {
            throw new IllegalStateException("asistencia.error04");
        }

        asistencia.setInicioDescanso(LocalDateTime.now());
        asistenciaRepository.save(asistencia);
    }

    @Transactional
    public void terminarDescanso(Long idEmpleado) {
        Asistencia asistencia = getJornadaAbierta(idEmpleado);

        if (asistencia.getInicioDescanso() == null) {
            throw new IllegalStateException("asistencia.error05");
        }

        long minutos = Duration.between(
                asistencia.getInicioDescanso(), LocalDateTime.now()).toMinutes();

        asistencia.setMinutosDescanso(asistencia.getMinutosDescanso() + minutos);
        asistencia.setInicioDescanso(null);
        asistenciaRepository.save(asistencia);
    }

    @Transactional
    public void marcarSalida(Long idEmpleado) {
        Asistencia asistencia = getJornadaAbierta(idEmpleado);

        if (asistencia.getInicioDescanso() != null) {
            throw new IllegalStateException("asistencia.error06");
        }

        LocalDateTime ahora = LocalDateTime.now();

        // Minutos trabajados = tiempo total de la jornada menos el descanso
        long minutos = Duration.between(asistencia.getEntrada(), ahora).toMinutes()
                - asistencia.getMinutosDescanso();

        if (minutos < 0) {
            minutos = 0;
        }

        asistencia.setSalida(ahora);
        asistencia.setMinutosTrabajados(minutos);
        asistenciaRepository.save(asistencia);
    }

    private Asistencia getJornadaAbierta(Long idEmpleado) {
        Optional<Asistencia> abierta = asistenciaRepository
                .findFirstByEmpleadoIdAndSalidaIsNullOrderByEntradaDesc(idEmpleado);

        if (abierta.isEmpty()) {
            throw new IllegalStateException("asistencia.error03");
        }

        return abierta.get();
    }
}
