package com.sgprest.controller;

import com.sgprest.service.AsistenciaService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/prenomina")
public class PrenominaController {

    private final AsistenciaService asistenciaService;

    @GetMapping
    public String listado(
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta,
            Model model) {

        if (hasta == null) {
            hasta = LocalDate.now();
        }
        if (desde == null) {
            desde = hasta.minusDays(14);
        }

        var asistencias = asistenciaService.getAsistencias(desde, hasta);

        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("totalAsistencias", asistencias.size());

        return "prenomina/listado";
    }
}
