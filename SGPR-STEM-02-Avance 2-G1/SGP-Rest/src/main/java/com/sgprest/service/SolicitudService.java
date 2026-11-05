package com.sgprest.service;

import com.sgprest.domain.Solicitud;
import com.sgprest.repository.EmpleadoRepository;
import com.sgprest.repository.SolicitudRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final EmpleadoRepository empleadoRepository;

    @Transactional(readOnly = true)
    public List<Solicitud> getSolicitudes(Long idEmpleado, boolean admin) {
        if (admin) {
            return solicitudRepository.findAll();
        }
        if (idEmpleado == null) {
            return new ArrayList<>();
        }
        return solicitudRepository.findByEmpleadoIdOrderByIdDesc(idEmpleado);
    }

    @Transactional
    public void save(Solicitud solicitud, Long idEmpleado) {
        if (!"VACACIONES".equals(solicitud.getTipo())
                && !"CAMBIO_TURNO".equals(solicitud.getTipo())) {
            throw new IllegalArgumentException("solicitud.error01");
        }

        if (solicitud.getFechaInicio() == null
                || solicitud.getFechaFin() == null
                || solicitud.getFechaFin().isBefore(solicitud.getFechaInicio())) {
            throw new IllegalArgumentException("solicitud.error02");
        }

        solicitud.setId(null);
        solicitud.setEmpleado(empleadoRepository.findById(idEmpleado).get());
        solicitud.setEstado("PENDIENTE");
        solicitudRepository.save(solicitud);
    }

    @Transactional
    public void decidir(Long idSolicitud, String decision) {
        if (!decision.equals("aprobar") && !decision.equals("rechazar")) {
            throw new IllegalArgumentException("solicitud.error04");
        }

        Solicitud solicitud = solicitudRepository.findById(idSolicitud).get();

        if (!"PENDIENTE".equals(solicitud.getEstado())) {
            throw new IllegalStateException("solicitud.error03");
        }

        if (decision.equals("aprobar")) {
            solicitud.setEstado("APROBADA");
        } else {
            solicitud.setEstado("RECHAZADA");
        }

        solicitudRepository.save(solicitud);
    }
}
