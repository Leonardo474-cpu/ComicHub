package com.comic.hub.service;

public interface ReporteService {
    byte[] generarReporteUsuariosPdf(String estado) throws Exception;

    byte[] generarReporteUsuariosPorPlanPdf() throws Exception;
    
    byte[] generarReporteSuscripcionesPdf() throws Exception;

    byte[] generarReporteComicsPdf() throws Exception;

    byte[] generarReporteCategoriasPdf() throws Exception;

    byte[] generarReporteAutoresPdf() throws Exception;
}
