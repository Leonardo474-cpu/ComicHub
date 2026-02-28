package com.comic.hub.controller;

import com.comic.hub.dto.response.ComicInicioResponseDto;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comic.hub.model.Comic;
import com.comic.hub.model.Suscripcion;
import com.comic.hub.model.Usuario;
import com.comic.hub.service.ComicService;
import com.comic.hub.service.SuscripcionService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<ComicInicioResponseDto> comicsIniciales = comicService.listarActivosParaInicio(0, 15).stream()
                .map(this::toComicInicioDto)
                .collect(Collectors.toList());
        model.addAttribute("comicsIniciales", comicsIniciales);

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

    @GetMapping("/api/comics/inicio")
    @ResponseBody
    public List<ComicInicioResponseDto> cargarComicsInicio(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "15") int size) {
        int pagina = Math.max(page, 0);
        int tamanio = Math.max(1, Math.min(size, 15));
        int offset = pagina * 15;
        if (offset >= 100) {
            return List.of();
        }

        int restante = 100 - offset;
        int limiteReal = Math.min(tamanio, restante);

        return comicService.listarActivosParaInicio(pagina, limiteReal).stream()
                .map(this::toComicInicioDto)
                .collect(Collectors.toList());
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

    private ComicInicioResponseDto toComicInicioDto(Comic comic) {
        String nombreAutor = comic.getAutor() != null ? comic.getAutor().getNombre() : "Autor no disponible";
        return new ComicInicioResponseDto(
                comic.getIdComic(),
                comic.getTitulo(),
                comic.getRutaImagenPortada(),
                nombreAutor,
                comic.getSinopsis());
    }
}
