package com.sgprest.controller;

import com.sgprest.domain.Turno;
import com.sgprest.service.TurnoService;
import com.sgprest.service.EmpleadoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/turno")
public class TurnoController {

    private final TurnoService turnoService;
    private final EmpleadoService empleadoService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var turnos = turnoService.getTurnos();

        model.addAttribute("turnos", turnos);
        model.addAttribute("totalTurnos", turnos.size());
        model.addAttribute("empleados", empleadoService.getEmpleados(true));

        return "turno/listado";
    }

    // Turnos del empleado que inició sesión
    @GetMapping("/mis")
    public String misTurnos(HttpSession session, Model model) {
        Long idEmpleado = (Long) session.getAttribute("empleadoId");
        if (idEmpleado == null) {
            return "redirect:/";
        }

        var turnos = turnoService.getTurnosEmpleado(idEmpleado);

        model.addAttribute("turnos", turnos);
        model.addAttribute("totalTurnos", turnos.size());

        return "turno/mis";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Turno turno,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage(
                            "turno.error04", null, Locale.getDefault()));

            return "redirect:/turno/listado";
        }

        try {
            turnoService.save(turno);

            redirectAttributes.addFlashAttribute(
                    "todoOk",
                    messageSource.getMessage(
                            "mensaje.actualizado", null, Locale.getDefault()));

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage(
                            "turno.error05", null, Locale.getDefault()));
        }

        return "redirect:/turno/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            turnoService.delete(id);

        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "turno.error01";

        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "turno.error02";

        } catch (Exception e) {
            titulo = "error";
            detalle = "turno.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(
                        detalle, null, Locale.getDefault()));

        return "redirect:/turno/listado";
    }
}
