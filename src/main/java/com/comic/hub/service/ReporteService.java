package com.comic.hub.service;

public interface ReporteService {
    byte[] generarReporteUsuariosPdf(String estado) throws Exception;

    byte[] generarReporteUsuariosPorPlanPdf() throws Exception;
}
