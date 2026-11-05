package com.sgprest.controller;

import com.sgprest.service.AsistenciaService;
import jakarta.servlet.http.HttpSession;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/asistencia")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;
    private final MessageSource messageSource;

    @GetMapping
    public String listado(HttpSession session, Model model) {
        Long idEmpleado = (Long) session.getAttribute("empleadoId");
        if (idEmpleado == null) {
            return "redirect:/";
        }

        var asistencias = asistenciaService.getAsistencias(idEmpleado);

        model.addAttribute("asistencias", asistencias);
        model.addAttribute("totalAsistencias", asistencias.size());

        return "asistencia/listado";
    }

    @PostMapping("/{accion}")
    public String marcar(
            @PathVariable String accion,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long idEmpleado = (Long) session.getAttribute("empleadoId");
        if (idEmpleado == null) {
            return "redirect:/";
        }

        String titulo = "todoOk";
        String detalle = "asistencia.marca";

        try {
            if (accion.equals("entrada")) {
                asistenciaService.marcarEntrada(idEmpleado);
            } else if (accion.equals("descanso")) {
                asistenciaService.iniciarDescanso(idEmpleado);
            } else if (accion.equals("regreso")) {
                asistenciaService.terminarDescanso(idEmpleado);
            } else if (accion.equals("salida")) {
                asistenciaService.marcarSalida(idEmpleado);
            } else {
                titulo = "error";
                detalle = "asistencia.error07";
            }

        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = e.getMessage();

        } catch (Exception e) {
            titulo = "error";
            detalle = "asistencia.error08";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(
                        detalle, null, Locale.getDefault()));

        return "redirect:/asistencia";
    }
}
