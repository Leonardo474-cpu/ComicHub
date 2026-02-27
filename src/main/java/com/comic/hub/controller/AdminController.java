package com.comic.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.comic.hub.model.Usuario;
import com.comic.hub.service.ComicService;
import com.comic.hub.service.SuscripcionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	private ComicService comicService;

	@Autowired
	private SuscripcionService suscripcionService;

	private boolean noEsAdmin(Usuario usuario) {
		return usuario == null || usuario.getRol() == null || !"ADMIN".equals(usuario.getRol().getNombreRol());
	}

	private void cargarDatosUsuario(Model model, Usuario usuario) {
		String nombreCompleto = usuario.getNombreCompleto() == null ? "" : usuario.getNombreCompleto().trim();
		String[] partes = nombreCompleto.isEmpty() ? new String[0] : nombreCompleto.split("\\s+");

		String nombre = partes.length > 0 ? partes[0] : "Admin";
		String apellido = partes.length > 1 ? partes[partes.length - 1] : "";

		model.addAttribute("nombre", nombre);
		model.addAttribute("apellido", apellido);
		model.addAttribute("nombreCompleto", nombreCompleto.isEmpty() ? "Administrador" : nombreCompleto);
	}

	@GetMapping("/admin")
	public String dashboard(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

		if (noEsAdmin(usuario)) {
			return "redirect:/login";
		}

		cargarDatosUsuario(model, usuario);
		return "admin/dashboard";
	}

	@GetMapping("/admin/comics")
	public String comics(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

		if (noEsAdmin(usuario)) {
			return "redirect:/login";
		}

		cargarDatosUsuario(model, usuario);
		model.addAttribute("comics", comicService.listarComics());

		return "admin/comics";
	}

	@GetMapping("/admin/suscripciones")
	public String suscripciones(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

		if (noEsAdmin(usuario)) {
			return "redirect:/login";
		}

		cargarDatosUsuario(model, usuario);
		model.addAttribute("suscripciones", suscripcionService.listarSuscripciones());

		return "admin/suscripciones";
	}
}