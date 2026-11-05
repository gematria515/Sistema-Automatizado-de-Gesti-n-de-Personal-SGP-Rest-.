package com.sgprest.repository;

import com.sgprest.domain.Solicitud;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository
        extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findByEmpleadoIdOrderByIdDesc(Long id);
}
