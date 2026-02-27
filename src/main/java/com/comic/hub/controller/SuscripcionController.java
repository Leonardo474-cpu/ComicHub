package com.comic.hub.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.comic.hub.model.Plan;
import com.comic.hub.model.Suscripcion;
import com.comic.hub.model.Usuario;
import com.comic.hub.service.SuscripcionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    public SuscripcionController(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    @GetMapping("/suscripciones")
    public String verSuscripciones(Model model, HttpSession session) {
        List<Plan> planes = suscripcionService.listarPlanesActivos();
        model.addAttribute("planes", planes);

        Optional<Integer> idPlanPopular = planes.stream()
                .max(Comparator.comparing(Plan::getPrecio, Comparator.nullsLast(Double::compareTo)))
                .map(Plan::getIdPlan);
        model.addAttribute("planPopularId", idPlanPopular.orElse(null));

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null) {
            Optional<Suscripcion> suscripcionActiva = suscripcionService.obtenerSuscripcionActiva(usuario.getId());
            model.addAttribute("suscripcionActiva", suscripcionActiva.orElse(null));
            model.addAttribute("tieneSuscripcionActiva", suscripcionActiva.isPresent());
            model.addAttribute("nombreUsuarioMenu", usuario.getNombreCompleto());
            model.addAttribute("nombrePlanMenu",
                    suscripcionActiva.map(s -> s.getPlan().getNombrePlan()).orElse(null));
        }
        return "suscripciones";
    }

    @PostMapping("/suscripciones/seleccionar")
    public String seleccionarPlan(@RequestParam Integer idPlan,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            session.setAttribute("postAuthRedirect", "/suscripciones");
            redirectAttributes.addFlashAttribute("registroError",
                    "Para continuar con el plan, primero debes registrarte o iniciar sesión.");
            redirectAttributes.addFlashAttribute("authModalOpen", true);
            redirectAttributes.addFlashAttribute("authTab", "register");
            return "redirect:/suscripciones";
        }

        try {
            suscripcionService.contratarPlan(idPlan, usuario);
            redirectAttributes.addFlashAttribute("suscripcionOk", "Plan activado correctamente.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("suscripcionError", ex.getMessage());
        }
        return "redirect:/suscripciones";
    }
}
