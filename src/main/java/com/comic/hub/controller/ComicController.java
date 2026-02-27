package com.comic.hub.controller;

import com.comic.hub.dto.request.ComicAdminRequestDto;
import com.comic.hub.dto.response.ComicListResponseDto;
import com.comic.hub.model.ComicEstado;
import com.comic.hub.service.ComicService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ComicController {

    private final ComicService comicService;

    public ComicController(ComicService comicService) {
        this.comicService = comicService;
    }

    @GetMapping("/admin/comics")
    public String listar(@RequestParam(defaultValue = "TODOS") String estado,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        Page<ComicListResponseDto> paginaComics = comicService.listarTodos(estado, page, 10);
        model.addAttribute("comics", paginaComics.getContent());
        model.addAttribute("pagina", paginaComics);
        model.addAttribute("estadoSeleccionado", estado.toUpperCase());
        return "admin/comics";
    }

    @GetMapping("/admin/comics/ver/{id}")
    public String verDetalle(@PathVariable Integer id, Model model) {
        model.addAttribute("comic", comicService.obtenerDetalle(id));
        return "admin/comics-detalle";
    }

    @GetMapping("/admin/comics/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("comic", new ComicAdminRequestDto());
        cargarCatalogos(model);
        return "admin/comics-form";
    }

    @GetMapping("/admin/comics/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("comic", comicService.buscarPorIdParaEdicion(id));
        cargarCatalogos(model);
        return "admin/comics-form";
    }

    @PostMapping("/admin/comics/guardar")
    public String guardar(@Valid @ModelAttribute("comic") ComicAdminRequestDto comic,
                          @RequestParam(value = "portada", required = false) MultipartFile portada,
                          BindingResult br,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (br.hasErrors()) {
            cargarCatalogos(model);
            return "admin/comics-form";
        }

        try {
            comicService.guardarDesdeAdmin(comic, portada);
        } catch (RuntimeException ex) {
            cargarCatalogos(model);
            model.addAttribute("error", ex.getMessage());
            return "admin/comics-form";
        }

        redirectAttributes.addFlashAttribute("success", "Comic guardado correctamente");
        return "redirect:/admin/comics";
    }

    @GetMapping("/admin/comics/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        comicService.cambiarEstado(id);
        return "redirect:/admin/comics?estadoActualizado=true";
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute("autores", comicService.listarAutoresActivos());
        model.addAttribute("categorias", comicService.listarCategoriasActivas());
        model.addAttribute("estadosComic", ComicEstado.values());
    }
}
