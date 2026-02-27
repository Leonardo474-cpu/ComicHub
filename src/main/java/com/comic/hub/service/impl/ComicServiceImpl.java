package com.comic.hub.service.impl;

import com.comic.hub.dto.request.ComicAdminRequestDto;
import com.comic.hub.dto.response.ComicListResponseDto;
import com.comic.hub.mapper.ComicMapper;
import com.comic.hub.model.Autor;
import com.comic.hub.model.Categoria;
import com.comic.hub.model.Comic;
import com.comic.hub.service.AzureBlobStorageService;
import com.comic.hub.repository.AutorRepository;
import com.comic.hub.repository.CategoriaRepository;
import com.comic.hub.repository.ComicRepository;
import com.comic.hub.service.ComicService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class ComicServiceImpl implements ComicService {

    private static final long MAX_FILE_SIZE_BYTES = 5L * 1024L * 1024L;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/png", "image/jpeg", "image/webp");

    private final ComicRepository comicRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final AzureBlobStorageService azureBlobStorageService;

    public ComicServiceImpl(
            ComicRepository comicRepository,
            AutorRepository autorRepository,
            CategoriaRepository categoriaRepository,
            AzureBlobStorageService azureBlobStorageService) {
        this.comicRepository = comicRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @Override
    public List<ComicListResponseDto> listarTodos() {
        return comicRepository.findAll().stream().map(ComicMapper::toListResponseDto).toList();
    }

    @Override
    public Comic obtenerDetalle(Integer idComic) {
        return comicRepository.findById(idComic)
                .orElseThrow(() -> new RuntimeException("Comic no encontrado"));
    }

    @Override
    public ComicAdminRequestDto buscarPorIdParaEdicion(Integer idComic) {
        Comic comic = comicRepository.findById(idComic)
                .orElseThrow(() -> new RuntimeException("Comic no encontrado"));
        return ComicMapper.toAdminRequestDto(comic);
    }

    @Override
    public void guardarDesdeAdmin(ComicAdminRequestDto comicAdminRequestDto, MultipartFile portada) {
        String tituloNormalizado = normalizarTexto(comicAdminRequestDto.getTitulo());
        if (tituloNormalizado.isEmpty()) {
            throw new RuntimeException("El titulo es obligatorio");
        }

        Autor autor = autorRepository.findById(comicAdminRequestDto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
        if (!autor.getActivo()) {
            throw new RuntimeException("No se puede asignar un autor inactivo");
        }

        Categoria categoria = categoriaRepository.findById(comicAdminRequestDto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        if (!categoria.getActivo()) {
            throw new RuntimeException("No se puede asignar una categoria inactiva");
        }

        boolean activoActual = true;
        String rutaPortadaActual = null;
        if (comicAdminRequestDto.getIdComic() != null) {
            Comic existente = comicRepository.findById(comicAdminRequestDto.getIdComic())
                    .orElseThrow(() -> new RuntimeException("Comic no encontrado"));
            activoActual = existente.getActivo();
            rutaPortadaActual = existente.getRutaImagenPortada();
        }

        comicAdminRequestDto.setTitulo(tituloNormalizado);
        comicAdminRequestDto.setSinopsis(normalizarTexto(comicAdminRequestDto.getSinopsis()));

        Comic comic = ComicMapper.toEntityForAdmin(comicAdminRequestDto, autor, categoria, activoActual);
        comic.setRutaImagenPortada(rutaPortadaActual);

        boolean portadaSeleccionada = portada != null
                && portada.getOriginalFilename() != null
                && !portada.getOriginalFilename().isBlank();

        String nuevaRutaPortada = null;
        if (portadaSeleccionada) {
            validarPortada(portada);
            nuevaRutaPortada = azureBlobStorageService.uploadImage(portada);
            comic.setRutaImagenPortada(nuevaRutaPortada);
        }

        try {
            comicRepository.save(comic);
        } catch (RuntimeException e) {
            if (nuevaRutaPortada != null) {
                azureBlobStorageService.deleteByUrl(nuevaRutaPortada);
            }
            throw new RuntimeException("No se pudo guardar el comic en base de datos");
        }

        if (nuevaRutaPortada != null && rutaPortadaActual != null && !rutaPortadaActual.isBlank()) {
            try {
                azureBlobStorageService.deleteByUrl(rutaPortadaActual);
            } catch (RuntimeException ignored) {
            }
        }
    }

    @Override
    public void cambiarEstado(Integer idComic) {
        Comic comic = comicRepository.findById(idComic)
                .orElseThrow(() -> new RuntimeException("Comic no encontrado"));
        comic.setActivo(!comic.getActivo());
        comicRepository.save(comic);
    }

    @Override
    public List<Autor> listarAutoresActivos() {
        return autorRepository.findAll().stream().filter(Autor::getActivo).toList();
    }

    @Override
    public List<Categoria> listarCategoriasActivas() {
        return categoriaRepository.findAll().stream().filter(Categoria::getActivo).toList();
    }

    private String normalizarTexto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private void validarPortada(MultipartFile portada) {
        if (portada == null || portada.isEmpty()) {
            throw new RuntimeException("Debe seleccionar una imagen de portada");
        }

        if (portada.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new RuntimeException("La portada supera el tamano maximo permitido de 5MB");
        }

        String contentType = portada.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new RuntimeException("Formato no permitido. Use PNG, JPEG o WEBP");
        }
    }
}
