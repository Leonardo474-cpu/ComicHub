package com.comic.hub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.comic.hub.model.Usuario;
import com.comic.hub.service.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;
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
        return "redirect:/home?auth=login";
    }

    // =========================
    //  PROCESAR LOGIN
    // =========================
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String password,
                                @RequestParam(name = "redirectTo", required = false) String redirectTo,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        try {
            String redirectSeguro = limpiarRedirect(redirectTo);
            if (redirectSeguro != null) {
                session.setAttribute("postAuthRedirect", redirectSeguro);
            }

            Usuario usuario = usuarioService.login(correo, password);
            session.setAttribute("usuarioLogueado", usuario);

            //  Redirección por rol
            if (usuario.getRol().getNombreRol().equals("ADMIN")) {
                session.removeAttribute("postAuthRedirect");
                return "redirect:/admin";
            } else {
                String postAuthRedirect = (String) session.getAttribute("postAuthRedirect");
                if (postAuthRedirect != null && !postAuthRedirect.isBlank()) {
                    session.removeAttribute("postAuthRedirect");
                    return "redirect:" + postAuthRedirect;
                }
                return "redirect:/home";
            }

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("loginError", e.getMessage());
            redirectAttributes.addFlashAttribute("authModalOpen", true);
            redirectAttributes.addFlashAttribute("authTab", "login");
            String postAuthRedirect = (String) session.getAttribute("postAuthRedirect");
            if (postAuthRedirect != null && !postAuthRedirect.isBlank()) {
                return "redirect:" + postAuthRedirect;
            }
            return "redirect:/home";
        }
    }

    private String limpiarRedirect(String redirectTo) {
        if (redirectTo == null) {
            return null;
        }
        String valor = redirectTo.trim();
        if (valor.isBlank() || !valor.startsWith("/") || valor.startsWith("//")) {
            return null;
        }
        return valor;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        return "redirect:/home";
    }
}
