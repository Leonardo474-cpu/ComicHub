package com.comic.hub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.comic.hub.model.Usuario;
import com.comic.hub.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // =========================
    //  MOSTRAR LOGIN
    // =========================
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // =========================
    //  PROCESAR LOGIN
    // =========================
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String password,
                                Model model,
                                HttpSession session) {

        try {

            Usuario usuario = usuarioService.login(correo, password);
            session.setAttribute("usuarioLogueado", usuario);

            //  Redirección por rol
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}



