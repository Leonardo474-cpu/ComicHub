package com.comic.hub.controller;

import com.comic.hub.dto.request.CategoriaAdminRequestDto;
import com.comic.hub.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/admin/categorias")
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodos());
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
}
