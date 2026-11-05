package com.sgprest.repository;

import com.sgprest.domain.Turno;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoRepository
        extends JpaRepository<Turno, Long> {

    List<Turno> findByEmpleadoIdOrderByFechaAsc(Long id);

    Optional<Turno> findFirstByEmpleadoIdAndFecha(Long id, LocalDate fecha);
}
