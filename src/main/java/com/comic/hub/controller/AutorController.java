package com.comic.hub.controller;

import com.comic.hub.dto.request.AutorAdminRequestDto;
import com.comic.hub.dto.response.AutorListResponseDto;
import com.comic.hub.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.IntStream;

@Controller
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping("/admin/autores")
    public String listar(@RequestParam(defaultValue = "TODOS") String estado,
                         @RequestParam(defaultValue = "") String q,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        Page<AutorListResponseDto> paginaAutores = autorService.listarTodos(estado, q, page, 10);
        model.addAttribute("autores", paginaAutores.getContent());
        model.addAttribute("pagina", paginaAutores);
        model.addAttribute("estadoSeleccionado", estado.toUpperCase());
        model.addAttribute("busqueda", q);
        model.addAttribute("pageNumbers", construirVentanaPaginas(paginaAutores));
        return "admin/autores";
    }

    @GetMapping("/admin/autores/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("autor", new AutorAdminRequestDto());
        return "admin/autores-form";
    }

    @GetMapping("/admin/autores/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("autor", autorService.buscarPorIdParaEdicion(id));
        return "admin/autores-form";
    }

    @PostMapping("/admin/autores/guardar")
    public String guardar(@Valid @ModelAttribute("autor") AutorAdminRequestDto autor,
                          BindingResult br,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (br.hasErrors()) {
            return "admin/autores-form";
        }

        try {
            autorService.guardarDesdeAdmin(autor);
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "admin/autores-form";
        }

        redirectAttributes.addFlashAttribute("success", "Autor guardado correctamente");
        return "redirect:/admin/autores";
    }

    @GetMapping("/admin/autores/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        autorService.cambiarEstado(id);
        return "redirect:/admin/autores?estadoActualizado=true";
    }

    private List<Integer> construirVentanaPaginas(Page<?> pagina) {
        int total = pagina.getTotalPages();
        if (total <= 0) {
            return List.of();
        }

        int actual = pagina.getNumber();
        int inicio = Math.max(0, actual - 2);
        int fin = Math.min(total - 1, actual + 2);

        if (fin - inicio < 4) {
            if (inicio == 0) {
                fin = Math.min(total - 1, inicio + 4);
            } else if (fin == total - 1) {
                inicio = Math.max(0, fin - 4);
            }
        }

        return IntStream.rangeClosed(inicio, fin).boxed().toList();
    }
}
