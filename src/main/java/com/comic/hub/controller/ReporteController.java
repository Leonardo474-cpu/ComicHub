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
    public ResponseEntity<byte[]> descargarReporteUsuarios(@RequestParam(defaultValue = "TODOS") String estado,
                                                           @RequestParam(defaultValue = "") String q) {
        try {
            byte[] reportePdf = reporteService.generarReporteUsuariosPdf(estado, q);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reporte_Usuarios_ComicHub.pdf");

            return ResponseEntity.ok().headers(headers).body(reportePdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/autores")
    public ResponseEntity<byte[]> descargarReporteAutores(@RequestParam(defaultValue = "TODOS") String estado,
                                                          @RequestParam(defaultValue = "") String q) {
        try {
            byte[] reportePdf = reporteService.generarReporteAutoresPdf(estado, q);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reporte_Autores_ComicHub.pdf");

            return ResponseEntity.ok().headers(headers).body(reportePdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categorias")
    public ResponseEntity<byte[]> descargarReporteCategorias(@RequestParam(defaultValue = "TODOS") String estado,
                                                             @RequestParam(defaultValue = "") String q) {
        try {
            byte[] reportePdf = reporteService.generarReporteCategoriasPdf(estado, q);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reporte_Categorias_ComicHub.pdf");

            return ResponseEntity.ok().headers(headers).body(reportePdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/comics")
    public ResponseEntity<byte[]> descargarReporteComics(@RequestParam(defaultValue = "TODOS") String estado,
                                                         @RequestParam(defaultValue = "") String q,
                                                         @RequestParam(required = false) String autorId,
                                                         @RequestParam(required = false) String categoriaId) {
        try {
            byte[] reportePdf = reporteService.generarReporteComicsPdf(
                    estado,
                    q,
                    parseNullableInteger(autorId),
                    parseNullableInteger(categoriaId)
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reporte_Comics_ComicHub.pdf");

            return ResponseEntity.ok().headers(headers).body(reportePdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/suscripciones")
    public ResponseEntity<byte[]> descargarReporteSuscripciones(@RequestParam(defaultValue = "TODOS") String estado,
                                                                @RequestParam(defaultValue = "") String q) {
        try {
            byte[] reportePdf = reporteService.generarReporteSuscripcionesPdf(estado, q);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reporte_Suscripciones_ComicHub.pdf");

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

    private Integer parseNullableInteger(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(valor);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
