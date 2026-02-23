package com.comic.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.comic.hub.model.Usuario;
import com.comic.hub.service.UsuarioService;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    // =========================
    // 🔹 MOSTRAR LOGIN
    // =========================
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // =========================
    // 🔹 PROCESAR LOGIN
    // =========================
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String password,
                                Model model) {

        try {

            Usuario usuario = usuarioService.login(correo, password);

            // 🔥 Redirección por rol
            if (usuario.getRol().getNombreRol().equals("ADMIN")) {
                return "redirect:/admin";
            } else {
                return "redirect:/home";
            }

        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
}
