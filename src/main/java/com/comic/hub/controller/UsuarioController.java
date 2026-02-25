package com.comic.hub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.comic.hub.dto.request.UsuarioAdminRequestDto;
import com.comic.hub.dto.request.UsuarioRegistroRequestDto;
import com.comic.hub.repository.RolRepository;
import com.comic.hub.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepo;

    public UsuarioController(UsuarioService usuarioService, RolRepository rolRepo) {
        this.usuarioService = usuarioService;
        this.rolRepo = rolRepo;
    }

    @GetMapping("/admin/usuarios")
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios";
    }

    @GetMapping("/registro")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroRequestDto());
        return "registro";
    }
    
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") UsuarioRegistroRequestDto usuario,
                            BindingResult br) {

        if (br.hasErrors()) {
            return "registro";
        }

        usuarioService.registrar(usuario);
        return "redirect:/login"; 
    }

    @PostMapping("/admin/usuarios/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") UsuarioAdminRequestDto usuario,
                          BindingResult br,
                          Model model) {

        if (br.hasErrors()) {
            model.addAttribute("roles", rolRepo.findAll());
            return "admin/usuarios-form";
        }

        usuarioService.guardarDesdeAdmin(usuario);
        return "redirect:/admin/usuarios";
    }
    

    @GetMapping("/admin/usuarios/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorIdParaEdicion(id));
        model.addAttribute("roles", rolRepo.findAll());
        return "admin/usuarios-form";
    }

    @GetMapping("/admin/usuarios/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return "redirect:/admin/usuarios";
    }
}
