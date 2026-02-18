package com.comic.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comic.hub.model.Usuario;
import com.comic.hub.repository.RolRepository;
import com.comic.hub.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepo;

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepo.findAll());
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Usuario usuario,
                          BindingResult br,
                          Model model) {

        if (br.hasErrors()) {
            model.addAttribute("roles", rolRepo.findAll());
            return "usuarios/form";
        }

        usuarioService.guardar(usuario);
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        model.addAttribute("roles", rolRepo.findAll());
        return "usuarios/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios/listar";
    }
}
