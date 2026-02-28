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

	@GetMapping("/suscripciones")
	public ResponseEntity<byte[]> descargarReporteSuscripciones() {
		try {
			byte[] reportePdf = reporteService.generarReporteSuscripcionesPdf();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "Reporte_Suscripciones_ComicHub.pdf");

			return ResponseEntity.ok().headers(headers).body(reportePdf);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/comics")
	public ResponseEntity<byte[]> descargarReporteComics() {
		try {
			byte[] reportePdf = reporteService.generarReporteComicsPdf();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "Reporte_Comics_ComicHub.pdf");

			return ResponseEntity.ok().headers(headers).body(reportePdf);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/categorias")
	public ResponseEntity<byte[]> descargarReporteCategorias() {
		try {
			byte[] reportePdf = reporteService.generarReporteCategoriasPdf();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "Reporte_Categorias_ComicHub.pdf");

			return ResponseEntity.ok().headers(headers).body(reportePdf);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/autores")
	public ResponseEntity<byte[]> descargarReporteAutores() {
		try {
			byte[] reportePdf = reporteService.generarReporteAutoresPdf();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "Reporte_Autores_ComicHub.pdf");

			return ResponseEntity.ok().headers(headers).body(reportePdf);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
