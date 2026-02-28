package com.comic.hub.controller;

import com.comic.hub.dto.request.CategoriaAdminRequestDto;
import com.comic.hub.dto.response.CategoriaListResponseDto;
import com.comic.hub.service.CategoriaService;
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
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/admin/categorias")
    public String listar(@RequestParam(defaultValue = "TODOS") String estado,
                         @RequestParam(defaultValue = "") String q,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        Page<CategoriaListResponseDto> paginaCategorias = categoriaService.listarTodos(estado, q, page, 10);
        model.addAttribute("categorias", paginaCategorias.getContent());
        model.addAttribute("pagina", paginaCategorias);
        model.addAttribute("estadoSeleccionado", estado.toUpperCase());
        model.addAttribute("busqueda", q);
        model.addAttribute("pageNumbers", construirVentanaPaginas(paginaCategorias));
        return "admin/categorias";
    }

    @GetMapping("/admin/categorias/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new CategoriaAdminRequestDto());
        return "admin/categorias-form";
    }

    @GetMapping("/admin/categorias/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", categoriaService.buscarPorIdParaEdicion(id));
        return "admin/categorias-form";
    }

    @PostMapping("/admin/categorias/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") CategoriaAdminRequestDto categoria,
                          BindingResult br,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (br.hasErrors()) {
            return "admin/categorias-form";
        }

        try {
            categoriaService.guardarDesdeAdmin(categoria);
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "admin/categorias-form";
        }

        redirectAttributes.addFlashAttribute("success", "Categoria guardada correctamente");
        return "redirect:/admin/categorias";
    }

    @GetMapping("/admin/categorias/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        categoriaService.cambiarEstado(id);
        return "redirect:/admin/categorias?estadoActualizado=true";
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
