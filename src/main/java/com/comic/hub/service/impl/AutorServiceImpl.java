package com.comic.hub.service.impl;

import com.comic.hub.dto.request.AutorAdminRequestDto;
import com.comic.hub.dto.response.AutorListResponseDto;
import com.comic.hub.mapper.AutorMapper;
import com.comic.hub.model.Autor;
import com.comic.hub.repository.AutorRepository;
import com.comic.hub.service.AutorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;

    public AutorServiceImpl(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public Page<AutorListResponseDto> listarTodos(String estado, String q, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), size);
        String terminoBusqueda = normalizarTexto(q);
        return obtenerPaginaPorEstadoYBusqueda(estado, terminoBusqueda, pageable).map(AutorMapper::toListResponseDto);
    }

    @Override
    public AutorAdminRequestDto buscarPorIdParaEdicion(Integer idAutor) {
        Autor autor = autorRepository.findById(idAutor)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
        return AutorMapper.toAdminRequestDto(autor);
    }

    @Override
    public void guardarDesdeAdmin(AutorAdminRequestDto autorAdminRequestDto) {
        String nombreNormalizado = normalizarTexto(autorAdminRequestDto.getNombre());
        if (nombreNormalizado.isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        autorRepository.findByNombreIgnoreCase(nombreNormalizado)
                .filter(a -> !a.getIdAutor().equals(autorAdminRequestDto.getIdAutor()))
                .ifPresent(a -> {
                    throw new RuntimeException("Ya existe un autor con ese nombre");
                });

        boolean activoActual = true;
        if (autorAdminRequestDto.getIdAutor() != null) {
            Autor existente = autorRepository.findById(autorAdminRequestDto.getIdAutor())
                    .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
            activoActual = existente.getActivo();
        }

        autorAdminRequestDto.setNombre(nombreNormalizado);
        autorAdminRequestDto.setSeudonimo(normalizarTexto(autorAdminRequestDto.getSeudonimo()));

        Autor autor = AutorMapper.toEntityForAdmin(autorAdminRequestDto, activoActual);
        autorRepository.save(autor);
    }

    @Override
    public void cambiarEstado(Integer idAutor) {
        Autor autor = autorRepository.findById(idAutor)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
        autor.setActivo(!autor.getActivo());
        autorRepository.save(autor);
    }

    private String normalizarTexto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private Page<Autor> obtenerPaginaPorEstadoYBusqueda(String estado, String busqueda, Pageable pageable) {
        boolean tieneBusqueda = busqueda != null && !busqueda.isBlank();

        if ("ACTIVOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? autorRepository.findByActivoAndNombreContainingIgnoreCase(true, busqueda, pageable)
                    : autorRepository.findByActivo(true, pageable);
        }
        if ("INACTIVOS".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? autorRepository.findByActivoAndNombreContainingIgnoreCase(false, busqueda, pageable)
                    : autorRepository.findByActivo(false, pageable);
        }
        return tieneBusqueda
                ? autorRepository.findByNombreContainingIgnoreCase(busqueda, pageable)
                : autorRepository.findAll(pageable);
    }
}
