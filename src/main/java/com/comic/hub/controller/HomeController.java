package com.comic.hub.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comic.hub.model.Suscripcion;
import com.comic.hub.model.Usuario;
import com.comic.hub.service.SuscripcionService;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
public class HomeController {

    private final SuscripcionService suscripcionService;

    public HomeController(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    @RequestMapping({"/", "/home"})
    public String mostrarHome(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null) {
            Optional<Suscripcion> suscripcionActiva = suscripcionService.obtenerSuscripcionActiva(usuario.getId());
            boolean tieneSuscripcionActiva = suscripcionActiva.isPresent();
            model.addAttribute("tieneSuscripcionActiva", tieneSuscripcionActiva);
            model.addAttribute("nombreUsuarioMenu", usuario.getNombreCompleto());
            model.addAttribute("nombrePlanMenu",
                    suscripcionActiva.map(s -> s.getPlan().getNombrePlan()).orElse(null));
        }
        return "home";
    }
}
