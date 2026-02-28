package com.comic.hub.service.impl;

import com.comic.hub.dto.response.PlanGraficoResponse;
import com.comic.hub.model.Suscripcion;
import com.comic.hub.repository.SuscripcionRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl {

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    public byte[] generarReporteUsuariosPdf() throws Exception {
    	// 1. Obtener todas las suscripciones
    	List<Suscripcion> todasLasSuscripciones = suscripcionRepository.findAll();

    	// 2. Agruparlas en un Mapa (K: Nombre del Plan, V: Cantidad)
    	Map<String, Long> conteoPorPlan = todasLasSuscripciones.stream()
    	    .collect(Collectors.groupingBy(
    	        // OJO: Si aquí te sale error, cambia getNombrePlan() por el getter exacto que tengas en tu clase Plan.java
    	        suscripcion -> suscripcion.getPlan().getNombrePlan(), 
    	        Collectors.counting()
    	    ));

    	// 3. Convertir el Mapa a tu lista de DTOs
    	List<PlanGraficoResponse> datosGrafico = conteoPorPlan.entrySet().stream()
    	    .map(entry -> new PlanGraficoResponse(entry.getKey(), entry.getValue()))
    	    .collect(Collectors.toList()); // Usamos esto en vez de .toList() por mayor compatibilidad en STS

        // 2. Cargar el archivo .jrxml desde la carpeta resources/reportes
        InputStream reporteStream = new ClassPathResource("reportes/usuarios.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reporteStream);

        // 3. Pasar la lista de DTOs al DataSource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datosGrafico);
        Map<String, Object> parametros = new HashMap<>();

        // 4. Llenar el reporte y exportarlo a PDF (arreglo de bytes)
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}