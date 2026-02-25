package com.comic.hub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.comic.hub.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String dashboard(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || usuario.getRol() == null || !"ADMIN".equals(usuario.getRol().getNombreRol())) {
            return "redirect:/login";
        }

        String nombreCompleto = usuario.getNombreCompleto() == null ? "" : usuario.getNombreCompleto().trim();
        String[] partes = nombreCompleto.isEmpty() ? new String[0] : nombreCompleto.split("\\s+");

        String nombre = partes.length > 0 ? partes[0] : "Admin";
        String apellido = partes.length > 1 ? partes[partes.length - 1] : "";

        model.addAttribute("nombre", nombre);
        model.addAttribute("apellido", apellido);
        model.addAttribute("nombreCompleto", nombreCompleto.isEmpty() ? "Administrador" : nombreCompleto);

        return "admin/dashboard";
    }
}
