package com.comic.hub.service.impl;

import com.comic.hub.dto.request.CategoriaAdminRequestDto;
import com.comic.hub.dto.response.CategoriaListResponseDto;
import com.comic.hub.mapper.CategoriaMapper;
import com.comic.hub.model.Categoria;
import com.comic.hub.repository.CategoriaRepository;
import com.comic.hub.service.CategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<CategoriaListResponseDto> listarTodos() {
        return categoriaRepository.findAll().stream().map(CategoriaMapper::toListResponseDto).toList();
    }

    @Override
    public CategoriaAdminRequestDto buscarPorIdParaEdicion(Integer idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        return CategoriaMapper.toAdminRequestDto(categoria);
    }

    @Override
    public void guardarDesdeAdmin(CategoriaAdminRequestDto categoriaAdminRequestDto) {
        String descripcionNormalizada = normalizarTexto(categoriaAdminRequestDto.getDescripcion());
        if (descripcionNormalizada.isEmpty()) {
            throw new RuntimeException("La descripcion es obligatoria");
        }

        categoriaRepository.findByDescripcionIgnoreCase(descripcionNormalizada)
                .filter(c -> !c.getIdCategoria().equals(categoriaAdminRequestDto.getIdCategoria()))
                .ifPresent(c -> {
                    throw new RuntimeException("Ya existe una categoria con esa descripcion");
                });

        boolean activoActual = true;
        if (categoriaAdminRequestDto.getIdCategoria() != null) {
            Categoria existente = categoriaRepository.findById(categoriaAdminRequestDto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
            activoActual = existente.getActivo();
        }

        categoriaAdminRequestDto.setDescripcion(descripcionNormalizada);

        Categoria categoria = CategoriaMapper.toEntityForAdmin(categoriaAdminRequestDto, activoActual);
        categoriaRepository.save(categoria);
    }

    @Override
    public void cambiarEstado(Integer idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        categoria.setActivo(!categoria.getActivo());
        categoriaRepository.save(categoria);
    }

    private String normalizarTexto(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
