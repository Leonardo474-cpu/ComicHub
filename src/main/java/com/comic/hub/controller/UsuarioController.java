package com.comic.hub.controller;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.comic.hub.repository.RolRepository;
import com.comic.hub.service.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
                            BindingResult br,
                            RedirectAttributes redirectAttributes) {

        if (br.hasErrors()) {
            redirectAttributes.addFlashAttribute("registroError", "Falta ingresar datos obligatorios correctamente");
            redirectAttributes.addFlashAttribute("authModalOpen", true);
            redirectAttributes.addFlashAttribute("authTab", "register");
            return "redirect:/home";
        }

        try {
            usuarioService.registrar(usuario);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("registroError", ex.getMessage());
            redirectAttributes.addFlashAttribute("authModalOpen", true);
            redirectAttributes.addFlashAttribute("authTab", "register");
            return "redirect:/home";
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
    
//    @GetMapping("/usuarios/pdf")
//    public void exportPDF(HttpServletResponse response) throws Exception{
//    	//Tipo de archivo
//    	response.setContentType("application/pdf");
//    	response.setHeader("Content-Disposition","inline;filename=usuarios.pdf");
//    	
//    	//Obtener la lista desde JPA de Spring
//    	List<Usuario> lista = usuarioService.listar();
//    	
//    	//Cargar JRXML desde Resources
//    	InputStream inputStream = getClass().getResourceAsStream("/reportes/usuarios.jrxml");
//    	
//    	//Compilar el .JRXML a .JASPER
//    	JasperReport jasperreport = JasperCompileManager.compileReport(inputStream);
//    	
//    	//Convertir la lista a Datasource
//    	JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
//    	
//    	//Llenar pdf
//    	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperreport,null, dataSource);
//    	
//    	//Exportar pdf
//    	JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
//    	
//    }

    @GetMapping("/admin/usuarios/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        usuarioService.cambiarEstado(id);
        return "redirect:/admin/usuarios?estadoActualizado=true";
    }
}
