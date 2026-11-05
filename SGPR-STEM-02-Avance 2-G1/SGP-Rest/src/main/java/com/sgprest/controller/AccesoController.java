package com.sgprest.controller;

import com.sgprest.domain.Empleado;
import com.sgprest.service.EmpleadoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AccesoController {

    private final EmpleadoService empleadoService;

    @GetMapping("/")
    public String inicio(HttpSession session) {
        if (session.getAttribute("rol") == null) {
            return "redirect:/login";
        }
        return "index";
    }

    // Pantalla de selección de perfil del prototipo
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("empleados", empleadoService.getEmpleados(true));
        return "login";
    }

    @PostMapping("/login")
    public String ingresar(
            @RequestParam String rol,
            @RequestParam(required = false) Long idEmpleado,
            HttpSession session) {

        if ("EMPLEADO".equals(rol)) {
            if (idEmpleado == null) {
                return "redirect:/login";
            }
            Empleado empleado = empleadoService.getEmpleado(idEmpleado).orElse(null);
            if (empleado == null) {
                return "redirect:/login";
            }
            session.setAttribute("usuario", empleado.getNombre());
            session.setAttribute("empleadoId", empleado.getId());
        } else {
            session.setAttribute("usuario", "Administrador");
            session.removeAttribute("empleadoId");
        }

        session.setAttribute("rol", rol);

        return "redirect:/";
    }

    @PostMapping("/salir")
    public String salir(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
