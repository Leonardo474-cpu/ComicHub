package com.comic.hub.mapper;

import com.comic.hub.dto.request.CategoriaAdminRequestDto;
import com.comic.hub.dto.response.CategoriaListResponseDto;
import com.comic.hub.model.Categoria;

public final class CategoriaMapper {

    private CategoriaMapper() {
    }

    public static CategoriaListResponseDto toListResponseDto(Categoria categoria) {
        CategoriaListResponseDto dto = new CategoriaListResponseDto();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setActivo(categoria.getActivo());
        return dto;
    }

    public static CategoriaAdminRequestDto toAdminRequestDto(Categoria categoria) {
        CategoriaAdminRequestDto dto = new CategoriaAdminRequestDto();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }

    public static Categoria toEntityForAdmin(CategoriaAdminRequestDto dto, boolean activoActual) {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(dto.getIdCategoria());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setActivo(activoActual);
        return categoria;
    }
}
