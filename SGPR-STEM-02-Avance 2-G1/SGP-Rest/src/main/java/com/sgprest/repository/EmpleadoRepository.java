package com.sgprest.repository;

import com.sgprest.domain.Empleado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository
        extends JpaRepository<Empleado, Long> {

    List<Empleado> findByActivoTrue();
}
