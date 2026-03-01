package com.comic.hub.service;

public interface ReporteService {
    byte[] generarReporteUsuariosPdf(String estado, String q) throws Exception;

    byte[] generarReporteAutoresPdf(String estado, String q) throws Exception;

    byte[] generarReporteCategoriasPdf(String estado, String q) throws Exception;

    byte[] generarReporteComicsPdf(String estado, String q, Integer autorId, Integer categoriaId) throws Exception;

    byte[] generarReporteSuscripcionesPdf(String estado, String q) throws Exception;

    byte[] generarReporteUsuariosPorPlanPdf() throws Exception;
}
