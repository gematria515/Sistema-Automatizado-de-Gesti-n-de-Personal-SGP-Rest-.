package com.sgprest.controller;

import com.sgprest.domain.Empleado;
import com.sgprest.service.EmpleadoService;
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
@RequestMapping("/empleado")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var empleados = empleadoService.getEmpleados(false);

        model.addAttribute("empleados", empleados);
        model.addAttribute("totalEmpleados", empleados.size());

        return "empleado/listado";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Empleado empleado,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage(
                            "empleado.error04", null, Locale.getDefault()));

            return "redirect:/empleado/listado";
        }

        try {
            empleadoService.save(empleado);

            redirectAttributes.addFlashAttribute(
                    "todoOk",
                    messageSource.getMessage(
                            "mensaje.actualizado", null, Locale.getDefault()));

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage(
                            "empleado.error05", null, Locale.getDefault()));
        }

        return "redirect:/empleado/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            empleadoService.delete(id);

        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "empleado.error01";

        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "empleado.error02";

        } catch (Exception e) {
            titulo = "error";
            detalle = "empleado.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(
                        detalle, null, Locale.getDefault()));

        return "redirect:/empleado/listado";
    }
}
