package com.comic.hub.service.impl;

import com.comic.hub.dto.response.PlanGraficoResponse;
import com.comic.hub.dto.response.UsuarioListResponseDto;
import com.comic.hub.mapper.UsuarioMapper;
import com.comic.hub.model.Suscripcion;
import com.comic.hub.model.Usuario;
import com.comic.hub.repository.SuscripcionRepository;
import com.comic.hub.repository.UsuarioRepository;
import com.comic.hub.service.ReporteService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public byte[] generarReporteUsuariosPdf(String estado) throws Exception {
        List<Usuario> usuarios = obtenerUsuariosPorEstado(estado);
        List<UsuarioListResponseDto> datosReporte = usuarios.stream()
                .map(UsuarioMapper::toListResponseDto)
                .collect(Collectors.toList());

        InputStream reporteStream = new ClassPathResource("reportes/usuarios.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reporteStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datosReporte);
        Map<String, Object> parametros = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] generarReporteUsuariosPorPlanPdf() throws Exception {
        List<Suscripcion> todasLasSuscripciones = suscripcionRepository.findAll();

        Map<String, Long> conteoPorPlan = todasLasSuscripciones.stream()
                .filter(suscripcion -> suscripcion.getPlan() != null && suscripcion.getPlan().getNombrePlan() != null)
                .collect(Collectors.groupingBy(
                        suscripcion -> suscripcion.getPlan().getNombrePlan(),
                        Collectors.counting()
                ));

        List<PlanGraficoResponse> datosGrafico = conteoPorPlan.entrySet().stream()
                .map(entry -> new PlanGraficoResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        InputStream reporteStream = new ClassPathResource("reportes/usuarios_plan.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reporteStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datosGrafico);
        Map<String, Object> parametros = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private List<Usuario> obtenerUsuariosPorEstado(String estado) {
        if ("ACTIVOS".equalsIgnoreCase(estado)) {
            return usuarioRepository.findByActivo(true, org.springframework.data.domain.Pageable.unpaged()).getContent();
        }
        if ("INACTIVOS".equalsIgnoreCase(estado)) {
            return usuarioRepository.findByActivo(false, org.springframework.data.domain.Pageable.unpaged()).getContent();
        }
        if ("TODOS".equalsIgnoreCase(estado)) {
            return usuarioRepository.findAll();
        }
        return Collections.emptyList();
    }
}
