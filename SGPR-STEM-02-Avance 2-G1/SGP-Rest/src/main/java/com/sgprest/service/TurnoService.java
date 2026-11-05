package com.sgprest.service;

import com.sgprest.domain.Turno;
import com.sgprest.repository.TurnoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;

    @Transactional(readOnly = true)
    public List<Turno> getTurnos() {
        return turnoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Turno> getTurnosEmpleado(Long idEmpleado) {
        return turnoRepository.findByEmpleadoIdOrderByFechaAsc(idEmpleado);
    }

    @Transactional(readOnly = true)
    public Optional<Turno> getTurno(Long id) {
        return turnoRepository.findById(id);
    }

    @Transactional
    public void save(Turno turno) {
        turnoRepository.save(turno);
    }

    @Transactional
    public void delete(Long id) {
        if (!turnoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El registro con ID " + id + " no existe.");
        }

        try {
            turnoRepository.deleteById(id);
            turnoRepository.flush();

        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar el registro. Tiene datos asociados.",
                    e);
        }
    }
}
