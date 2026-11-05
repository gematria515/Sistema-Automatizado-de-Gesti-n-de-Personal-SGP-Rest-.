package com.sgprest.repository;

import com.sgprest.domain.Asistencia;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsistenciaRepository
        extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByEmpleadoIdOrderByEntradaDesc(Long id);

    Optional<Asistencia> findFirstByEmpleadoIdAndSalidaIsNullOrderByEntradaDesc(Long id);

    List<Asistencia> findByEntradaBetweenOrderByEntradaAsc(
            LocalDateTime inicio, LocalDateTime fin);
}
