package com.sgprest.controller;

import com.sgprest.domain.Solicitud;
import com.sgprest.service.SolicitudService;
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
@RequestMapping("/solicitud")
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(HttpSession session, Model model) {
        Long idEmpleado = (Long) session.getAttribute("empleadoId");
        boolean admin = "ADMIN".equals(session.getAttribute("rol"));

        var solicitudes = solicitudService.getSolicitudes(idEmpleado, admin);

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("totalSolicitudes", solicitudes.size());

        return "solicitud/listado";
    }

    @PostMapping("/guardar")
    public String guardar(
            Solicitud solicitud,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long idEmpleado = (Long) session.getAttribute("empleadoId");
        if (idEmpleado == null) {
            return "redirect:/";
        }

        String titulo = "todoOk";
        String detalle = "solicitud.registrada";

        try {
            solicitudService.save(solicitud, idEmpleado);

        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = e.getMessage();

        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = e.getMessage();

        } catch (Exception e) {
            titulo = "error";
            detalle = "solicitud.error05";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(
                        detalle, null, Locale.getDefault()));

        return "redirect:/solicitud/listado";
    }

    @PostMapping("/{id}/{decision}")
    public String decidir(
            @PathVariable Long id,
            @PathVariable String decision,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!"ADMIN".equals(session.getAttribute("rol"))) {
            return "redirect:/";
        }

        String titulo = "todoOk";
        String detalle = "solicitud.actualizada";

        try {
            solicitudService.decidir(id, decision);

        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = e.getMessage();

        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = e.getMessage();

        } catch (Exception e) {
            titulo = "error";
            detalle = "solicitud.error05";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(
                        detalle, null, Locale.getDefault()));

        return "redirect:/solicitud/listado";
    }
}
