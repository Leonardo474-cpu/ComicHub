package com.comic.hub.controller;

import com.comic.hub.service.ReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<byte[]> descargarReporteUsuarios(@RequestParam(defaultValue = "TODOS") String estado) {
        try {
            byte[] reportePdf = reporteService.generarReporteUsuariosPdf(estado);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reporte_Usuarios_ComicHub.pdf");

            return ResponseEntity.ok().headers(headers).body(reportePdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/usuarios-plan")
    public ResponseEntity<byte[]> descargarReporteUsuariosPlan() {
        try {
            byte[] reportePdf = reporteService.generarReporteUsuariosPorPlanPdf();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reporte_Usuarios_Por_Plan_ComicHub.pdf");

            return ResponseEntity.ok().headers(headers).body(reportePdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
