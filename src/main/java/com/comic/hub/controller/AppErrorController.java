package com.comic.hub.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comic.hub.model.Suscripcion;
import com.comic.hub.model.Usuario;
import com.comic.hub.service.SuscripcionService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AppErrorController {

    private final SuscripcionService suscripcionService;

    public AppErrorController(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    @RequestMapping("/app-error")
    public String manejarError(HttpServletRequest request, HttpSession session, Model model) {
        Object statusAttr = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = statusAttr == null ? 500 : Integer.parseInt(statusAttr.toString());

        String uri = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        if (uri == null || "null".equals(uri)) {
            uri = request.getRequestURI();
        }

        model.addAttribute("rutaSolicitada", uri);

        if (status == 404) {
            if (uri != null && uri.startsWith("/admin")) {
                return "admin/error-404";
            }
            cargarDatosCliente(model, session);
            return "error/404-client";
        }

        if (uri != null && uri.startsWith("/admin")) {
            return "admin/error-404";
        }
        cargarDatosCliente(model, session);
        return "error/404-client";
    }

    private void cargarDatosCliente(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            model.addAttribute("tieneSuscripcionActiva", false);
            return;
        }

        Optional<Suscripcion> suscripcionActiva = suscripcionService.obtenerSuscripcionActiva(usuario.getId());
        model.addAttribute("tieneSuscripcionActiva", suscripcionActiva.isPresent());
        model.addAttribute("nombreUsuarioMenu", usuario.getNombreCompleto());
        model.addAttribute("nombrePlanMenu", suscripcionActiva.map(s -> s.getPlan().getNombrePlan()).orElse(null));
    }
}
