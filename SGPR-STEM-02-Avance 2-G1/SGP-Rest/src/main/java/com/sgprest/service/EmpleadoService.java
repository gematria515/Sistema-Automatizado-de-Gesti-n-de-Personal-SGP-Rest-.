package com.sgprest.service;

import com.sgprest.domain.Empleado;
import com.sgprest.repository.EmpleadoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Transactional(readOnly = true)
    public List<Empleado> getEmpleados(boolean activo) {
        if (activo) {
            return empleadoRepository.findByActivoTrue();
        }
        return empleadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Empleado> getEmpleado(Long id) {
        return empleadoRepository.findById(id);
    }

    @Transactional
    public void save(Empleado empleado) {
        empleadoRepository.save(empleado);
    }

    @Transactional
    public void delete(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El registro con ID " + id + " no existe.");
        }

        try {
            empleadoRepository.deleteById(id);
            empleadoRepository.flush();

        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar el registro. Tiene datos asociados.",
                    e);
        }
    }
}
