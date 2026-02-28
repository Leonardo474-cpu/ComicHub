package com.comic.hub.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comic.hub.model.Comic;
import com.comic.hub.model.Suscripcion;
import com.comic.hub.model.Usuario;
import com.comic.hub.service.ComicService;
import com.comic.hub.service.SuscripcionService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final SuscripcionService suscripcionService;
    private final ComicService comicService;

    public HomeController(SuscripcionService suscripcionService, ComicService comicService) {
        this.suscripcionService = suscripcionService;
        this.comicService = comicService;
    }

    @RequestMapping({"/", "/home"})
    public String mostrarHome(Model model, HttpSession session) {
        List<Comic> comicsActivos = comicService.listarActivosParaInicio(12);
        model.addAttribute("comicsActivos", comicsActivos);

        Object usuarioSesion = session.getAttribute("usuarioLogueado");
        if (usuarioSesion instanceof Usuario usuario) {
            Optional<Suscripcion> suscripcionActiva = suscripcionService.obtenerSuscripcionActiva(usuario.getId());
            boolean tieneSuscripcionActiva = suscripcionActiva.isPresent();
            model.addAttribute("tieneSuscripcionActiva", tieneSuscripcionActiva);
            model.addAttribute("nombreUsuarioMenu", usuario.getNombreCompleto());
            model.addAttribute("nombrePlanMenu",
                    suscripcionActiva.map(s -> s.getPlan().getNombrePlan()).orElse(null));
        }
        return "home";
    }

    @GetMapping("/comics/{id}")
    public String verDetalleComic(@PathVariable Integer id, Model model, HttpSession session) {
        Comic comic;
        try {
            comic = comicService.obtenerDetalle(id);
        } catch (RuntimeException ex) {
            return "redirect:/home";
        }

        if (!comic.getActivo()) {
            return "redirect:/home";
        }

        model.addAttribute("comic", comic);

        Object usuarioSesion = session.getAttribute("usuarioLogueado");
        if (usuarioSesion instanceof Usuario usuario) {
            Optional<Suscripcion> suscripcionActiva = suscripcionService.obtenerSuscripcionActiva(usuario.getId());
            model.addAttribute("tieneSuscripcionActiva", suscripcionActiva.isPresent());
            model.addAttribute("nombreUsuarioMenu", usuario.getNombreCompleto());
            model.addAttribute("nombrePlanMenu", suscripcionActiva.map(s -> s.getPlan().getNombrePlan()).orElse(null));
        }

        return "comic-detalle";
    }
}
