package com.comic.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.comic.hub.model.Usuario;
import com.comic.hub.service.SuscripcionService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.IntStream;

@Controller
public class AdminController {

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

	@GetMapping("/admin/suscripciones")
	public String suscripciones(@RequestParam(defaultValue = "TODOS") String estado,
								@RequestParam(defaultValue = "") String q,
								@RequestParam(defaultValue = "0") int page,
								Model model,
								HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

		if (noEsAdmin(usuario)) {
			return "redirect:/login";
		}

		cargarDatosUsuario(model, usuario);
		Page<com.comic.hub.model.Suscripcion> paginaSuscripciones = suscripcionService.listarSuscripciones(estado, q, page, 10);
		model.addAttribute("suscripciones", paginaSuscripciones.getContent());
		model.addAttribute("pagina", paginaSuscripciones);
		model.addAttribute("estadoSeleccionado", estado.toUpperCase());
		model.addAttribute("busqueda", q);
		model.addAttribute("pageNumbers", construirVentanaPaginas(paginaSuscripciones));

		return "admin/suscripciones";
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
