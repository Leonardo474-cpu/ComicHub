package com.comic.hub.service.impl;

import com.comic.hub.dto.response.AutorListResponseDto;
import com.comic.hub.dto.response.CategoriaListResponseDto;
import com.comic.hub.dto.response.ComicListResponseDto;
import com.comic.hub.dto.response.PlanGraficoResponse;
import com.comic.hub.dto.response.UsuarioListResponseDto;
import com.comic.hub.mapper.AutorMapper;
import com.comic.hub.mapper.CategoriaMapper;
import com.comic.hub.mapper.ComicMapper;
import com.comic.hub.mapper.UsuarioMapper;
import com.comic.hub.model.Comic;
import com.comic.hub.model.Suscripcion;
import com.comic.hub.model.Usuario;
import com.comic.hub.repository.AutorRepository;
import com.comic.hub.repository.CategoriaRepository;
import com.comic.hub.repository.ComicRepository;
import com.comic.hub.repository.SuscripcionRepository;
import com.comic.hub.repository.UsuarioRepository;
import com.comic.hub.service.ReporteService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final ComicRepository comicRepository;

    public ReporteServiceImpl(SuscripcionRepository suscripcionRepository,
                              UsuarioRepository usuarioRepository,
                              AutorRepository autorRepository,
                              CategoriaRepository categoriaRepository,
                              ComicRepository comicRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.comicRepository = comicRepository;
    }

    @Override
    public byte[] generarReporteUsuariosPdf(String estado, String q) throws Exception {
        List<UsuarioListResponseDto> datosReporte = obtenerUsuariosFiltrados(estado, q).stream()
                .map(UsuarioMapper::toListResponseDto)
                .toList();
        return exportarReporte("reportes/usuarios.jrxml", datosReporte);
    }

    @Override
    public byte[] generarReporteAutoresPdf(String estado, String q) throws Exception {
        List<AutorListResponseDto> datosReporte = obtenerAutoresFiltrados(estado, q).stream()
                .map(AutorMapper::toListResponseDto)
                .toList();
        return exportarReporte("reportes/autores.jrxml", datosReporte);
    }

    @Override
    public byte[] generarReporteCategoriasPdf(String estado, String q) throws Exception {
        List<CategoriaListResponseDto> datosReporte = obtenerCategoriasFiltradas(estado, q).stream()
                .map(CategoriaMapper::toListResponseDto)
                .toList();
        return exportarReporte("reportes/categoria.jrxml", datosReporte);
    }

    @Override
    public byte[] generarReporteComicsPdf(String estado, String q, Integer autorId, Integer categoriaId) throws Exception {
        List<ComicReporteDto> datosReporte = obtenerComicsFiltrados(estado, q, autorId, categoriaId).stream()
                .map(ComicMapper::toListResponseDto)
                .map(ComicReporteDto::fromDto)
                .toList();
        return exportarReporte("reportes/comics.jrxml", datosReporte);
    }

    @Override
    public byte[] generarReporteSuscripcionesPdf(String estado, String q) throws Exception {
        List<SuscripcionReporteDto> datosReporte = obtenerSuscripcionesFiltradas(estado, q).stream()
                .map(SuscripcionReporteDto::fromEntity)
                .toList();
        return exportarReporte("reportes/suscripciones.jrxml", datosReporte);
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
                .toList();

        return exportarReporte("reportes/usuarios_plan.jrxml", datosGrafico);
    }

    private byte[] exportarReporte(String rutaJrxml, List<?> data) throws Exception {
        InputStream reporteStream = new ClassPathResource(rutaJrxml).getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reporteStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private List<Usuario> obtenerUsuariosFiltrados(String estado, String q) {
        String busqueda = normalizarTexto(q);
        boolean tieneBusqueda = !busqueda.isBlank();

        if ("ACTIVOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? usuarioRepository.findByActivoAndNombreCompletoContainingIgnoreCase(true, busqueda, Pageable.unpaged()).getContent()
                    : usuarioRepository.findByActivo(true, Pageable.unpaged()).getContent();
        }
        if ("INACTIVOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? usuarioRepository.findByActivoAndNombreCompletoContainingIgnoreCase(false, busqueda, Pageable.unpaged()).getContent()
                    : usuarioRepository.findByActivo(false, Pageable.unpaged()).getContent();
        }
        if ("TODOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? usuarioRepository.findByNombreCompletoContainingIgnoreCase(busqueda, Pageable.unpaged()).getContent()
                    : usuarioRepository.findAll();
        }
        return Collections.emptyList();
    }

    private List<com.comic.hub.model.Autor> obtenerAutoresFiltrados(String estado, String q) {
        String busqueda = normalizarTexto(q);
        boolean tieneBusqueda = !busqueda.isBlank();

        if ("ACTIVOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? autorRepository.findByActivoAndNombreContainingIgnoreCase(true, busqueda, Pageable.unpaged()).getContent()
                    : autorRepository.findByActivo(true, Pageable.unpaged()).getContent();
        }
        if ("INACTIVOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? autorRepository.findByActivoAndNombreContainingIgnoreCase(false, busqueda, Pageable.unpaged()).getContent()
                    : autorRepository.findByActivo(false, Pageable.unpaged()).getContent();
        }
        if ("TODOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? autorRepository.findByNombreContainingIgnoreCase(busqueda, Pageable.unpaged()).getContent()
                    : autorRepository.findAll();
        }
        return Collections.emptyList();
    }

    private List<com.comic.hub.model.Categoria> obtenerCategoriasFiltradas(String estado, String q) {
        String busqueda = normalizarTexto(q);
        boolean tieneBusqueda = !busqueda.isBlank();

        if ("ACTIVOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? categoriaRepository.findByActivoAndDescripcionContainingIgnoreCase(true, busqueda, Pageable.unpaged()).getContent()
                    : categoriaRepository.findByActivo(true, Pageable.unpaged()).getContent();
        }
        if ("INACTIVOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? categoriaRepository.findByActivoAndDescripcionContainingIgnoreCase(false, busqueda, Pageable.unpaged()).getContent()
                    : categoriaRepository.findByActivo(false, Pageable.unpaged()).getContent();
        }
        if ("TODOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? categoriaRepository.findByDescripcionContainingIgnoreCase(busqueda, Pageable.unpaged()).getContent()
                    : categoriaRepository.findAll();
        }
        return Collections.emptyList();
    }

    private List<Comic> obtenerComicsFiltrados(String estado, String q, Integer autorId, Integer categoriaId) {
        return comicRepository.buscarConFiltros(
                mapearEstadoAActivo(estado),
                normalizarTexto(q),
                autorId,
                categoriaId,
                Pageable.unpaged()
        ).getContent();
    }

    private List<Suscripcion> obtenerSuscripcionesFiltradas(String estado, String q) {
        String busqueda = normalizarTexto(q);
        boolean tieneBusqueda = !busqueda.isBlank();

        if ("ACTIVA".equalsIgnoreCase(estado) || "FINALIZADA".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? suscripcionRepository.findByEstadoIgnoreCaseAndUsuario_NombreCompletoContainingIgnoreCase(
                    estado,
                    busqueda,
                    Pageable.unpaged()
            ).getContent()
                    : suscripcionRepository.findByEstadoIgnoreCase(estado, Pageable.unpaged()).getContent();
        }

        return tieneBusqueda
                ? suscripcionRepository.findByUsuario_NombreCompletoContainingIgnoreCase(busqueda, Pageable.unpaged()).getContent()
                : suscripcionRepository.findAll();
    }

    private String normalizarTexto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private Boolean mapearEstadoAActivo(String estado) {
        if ("ACTIVOS".equalsIgnoreCase(estado)) {
            return true;
        }
        if ("INACTIVOS".equalsIgnoreCase(estado)) {
            return false;
        }
        return null;
    }

    public static class SuscripcionReporteDto {
        private Integer idSuscripcion;
        private java.time.LocalDate fechaInicio;
        private java.time.LocalDate fechaFin;
        private String estado;
        private java.math.BigDecimal precioAplicado;
        private String usuario;
        private String plan;

        public static SuscripcionReporteDto fromEntity(Suscripcion suscripcion) {
            SuscripcionReporteDto dto = new SuscripcionReporteDto();
            dto.setIdSuscripcion(suscripcion.getIdSuscripcion());
            dto.setFechaInicio(suscripcion.getFechaInicio());
            dto.setFechaFin(suscripcion.getFechaFin());
            dto.setEstado(suscripcion.getEstado());
            dto.setPrecioAplicado(suscripcion.getPrecioAplicado());
            dto.setUsuario(suscripcion.getUsuario() != null ? suscripcion.getUsuario().getNombreCompleto() : "");
            dto.setPlan(suscripcion.getPlan() != null ? suscripcion.getPlan().getNombrePlan() : "");
            return dto;
        }

        public Integer getIdSuscripcion() {
            return idSuscripcion;
        }

        public void setIdSuscripcion(Integer idSuscripcion) {
            this.idSuscripcion = idSuscripcion;
        }

        public java.time.LocalDate getFechaInicio() {
            return fechaInicio;
        }

        public void setFechaInicio(java.time.LocalDate fechaInicio) {
            this.fechaInicio = fechaInicio;
        }

        public java.time.LocalDate getFechaFin() {
            return fechaFin;
        }

        public void setFechaFin(java.time.LocalDate fechaFin) {
            this.fechaFin = fechaFin;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public java.math.BigDecimal getPrecioAplicado() {
            return precioAplicado;
        }

        public void setPrecioAplicado(java.math.BigDecimal precioAplicado) {
            this.precioAplicado = precioAplicado;
        }

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }
    }

    public static class ComicReporteDto {
        private Integer idComic;
        private String titulo;
        private String autor;
        private String categoria;
        private String estado;

        public static ComicReporteDto fromDto(ComicListResponseDto dto) {
            ComicReporteDto reporte = new ComicReporteDto();
            reporte.setIdComic(dto.getIdComic());
            reporte.setTitulo(dto.getTitulo());
            reporte.setAutor(dto.getAutor());
            reporte.setCategoria(dto.getCategoria());
            reporte.setEstado(dto.getEstado() != null ? dto.getEstado().name() : "");
            return reporte;
        }

        public Integer getIdComic() {
            return idComic;
        }

        public void setIdComic(Integer idComic) {
            this.idComic = idComic;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getAutor() {
            return autor;
        }

        public void setAutor(String autor) {
            this.autor = autor;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
}
