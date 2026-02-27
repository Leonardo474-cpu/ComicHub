package com.comic.hub.controller;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.comic.hub.dto.request.UsuarioAdminRequestDto;
import com.comic.hub.dto.request.UsuarioRegistroRequestDto;
import com.comic.hub.dto.response.UsuarioListResponseDto;
import com.comic.hub.repository.RolRepository;
import com.comic.hub.service.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
    
    @GetMapping("/usuarios/pdf")
    public void exportPDF(HttpServletResponse response) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=usuarios.pdf");

        try (InputStream inputStream = getClass().getResourceAsStream("/reportes/usuarios.jrxml")) {
            if (inputStream == null) {
                throw new RuntimeException("No se encontró el archivo usuarios.jrxml en /resources/reportes/");
            }

            List<UsuarioListResponseDto> lista = usuarioService.listarTodos();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage());
        }
    }

    @GetMapping("/admin/usuarios/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return "redirect:/admin/usuarios";
    }
}
