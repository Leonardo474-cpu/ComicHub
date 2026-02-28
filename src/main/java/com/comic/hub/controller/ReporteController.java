package com.comic.hub.controller;

import com.comic.hub.service.impl.ReporteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteServiceImpl reporteService;

    @GetMapping("/usuarios-plan")
    public ResponseEntity<byte[]> descargarReporteUsuarios() {
        try {
            byte[] reportePdf = reporteService.generarReporteUsuariosPdf();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Para que se abra en el navegador en vez de forzar la descarga, usa "inline"
            headers.setContentDispositionFormData("inline", "Reporte_Usuarios_ComicHub.pdf");

            return ResponseEntity.ok().headers(headers).body(reportePdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}