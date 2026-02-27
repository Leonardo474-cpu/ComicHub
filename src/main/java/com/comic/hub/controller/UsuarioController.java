package com.comic.hub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.comic.hub.dto.request.UsuarioAdminRequestDto;
import com.comic.hub.dto.request.UsuarioRegistroRequestDto;
import com.comic.hub.dto.response.UsuarioListResponseDto;
import com.comic.hub.model.Usuario;
import com.comic.hub.repository.RolRepository;
import com.comic.hub.service.UsuarioService;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepo;

    public UsuarioController(UsuarioService usuarioService, RolRepository rolRepo) {
        this.usuarioService = usuarioService;
        this.rolRepo = rolRepo;
    }

    @GetMapping("/admin/usuarios")
    public String listar(@RequestParam(defaultValue = "TODOS") String estado,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        Page<UsuarioListResponseDto> paginaUsuarios = usuarioService.listarTodos(estado, page, 10);
        model.addAttribute("usuarios", paginaUsuarios.getContent());
        model.addAttribute("pagina", paginaUsuarios);
        model.addAttribute("estadoSeleccionado", estado.toUpperCase());
        return "admin/usuarios";
    }

    @GetMapping("/admin/usuarios/nuevo")
    public String nuevoAdmin(Model model) {
        model.addAttribute("usuario", new UsuarioAdminRequestDto());
        model.addAttribute("roles", rolRepo.findAll());
        return "admin/usuarios-form";
    }

    @GetMapping("/registro")
    public String nuevo() {
        return "redirect:/home?auth=register";
    }
    
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") UsuarioRegistroRequestDto usuario,
                            @RequestParam(name = "redirectTo", required = false) String redirectTo,
                            BindingResult br,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        String redirectSeguro = limpiarRedirect(redirectTo);
        if (redirectSeguro != null) {
            session.setAttribute("postAuthRedirect", redirectSeguro);
        }

        if (br.hasErrors()) {
            redirectAttributes.addFlashAttribute("registroError", "Falta ingresar datos obligatorios correctamente");
            redirectAttributes.addFlashAttribute("authModalOpen", true);
            redirectAttributes.addFlashAttribute("authTab", "register");
            return redireccionConContexto(session);
        }

        try {
            usuarioService.registrar(usuario);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("registroError", ex.getMessage());
            redirectAttributes.addFlashAttribute("authModalOpen", true);
            redirectAttributes.addFlashAttribute("authTab", "register");
            return redireccionConContexto(session);
        }

        String postAuthRedirect = (String) session.getAttribute("postAuthRedirect");
        if (postAuthRedirect != null && !postAuthRedirect.isBlank()) {
            try {
                Usuario usuarioLogueado = usuarioService.login(usuario.getCorreo(), usuario.getPassword());
                session.setAttribute("usuarioLogueado", usuarioLogueado);
                session.removeAttribute("postAuthRedirect");
                redirectAttributes.addFlashAttribute("suscripcionOk", "Registro completado. Ya puedes elegir tu plan.");
                return "redirect:" + postAuthRedirect;
            } catch (RuntimeException ex) {
                redirectAttributes.addFlashAttribute("registroOk", "Cuenta registrada correctamente. Ahora inicia sesión.");
                redirectAttributes.addFlashAttribute("loginError", "Tu cuenta fue creada, pero debes iniciar sesión para continuar.");
                redirectAttributes.addFlashAttribute("authModalOpen", true);
                redirectAttributes.addFlashAttribute("authTab", "login");
                return "redirect:" + postAuthRedirect;
            }
        }

        redirectAttributes.addFlashAttribute("registroOk", "Cuenta registrada correctamente. Ahora inicia sesión.");
        redirectAttributes.addFlashAttribute("authModalOpen", true);
        redirectAttributes.addFlashAttribute("authTab", "login");
        return "redirect:/home";
    }

    @PostMapping("/admin/usuarios/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") UsuarioAdminRequestDto usuario,
                          BindingResult br,
                          Model model) {

        if (br.hasErrors()) {
            model.addAttribute("roles", rolRepo.findAll());
            return "admin/usuarios-form";
        }

        try {
            usuarioService.guardarDesdeAdmin(usuario);
        } catch (RuntimeException ex) {
            model.addAttribute("roles", rolRepo.findAll());
            model.addAttribute("error", ex.getMessage());
            return "admin/usuarios-form";
        }
        return "redirect:/admin/usuarios";
    }
    

    @GetMapping("/admin/usuarios/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorIdParaEdicion(id));
        model.addAttribute("roles", rolRepo.findAll());
        return "admin/usuarios-form";
    }

    @GetMapping("/admin/usuarios/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        usuarioService.cambiarEstado(id);
        return "redirect:/admin/usuarios?estadoActualizado=true";
    }

    private String redireccionConContexto(HttpSession session) {
        String postAuthRedirect = (String) session.getAttribute("postAuthRedirect");
        if (postAuthRedirect != null && !postAuthRedirect.isBlank()) {
            return "redirect:" + postAuthRedirect;
        }
        return "redirect:/home";
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
}
