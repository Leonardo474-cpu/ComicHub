package com.comic.hub.mapper;

import com.comic.hub.dto.request.AutorAdminRequestDto;
import com.comic.hub.dto.response.AutorListResponseDto;
import com.comic.hub.model.Autor;

public final class AutorMapper {

    private AutorMapper() {
    }

    public static AutorListResponseDto toListResponseDto(Autor autor) {
        AutorListResponseDto dto = new AutorListResponseDto();
        dto.setIdAutor(autor.getIdAutor());
        dto.setNombre(autor.getNombre());
        dto.setSeudonimo(autor.getSeudonimo());
        dto.setActivo(autor.getActivo());
        return dto;
    }

    public static AutorAdminRequestDto toAdminRequestDto(Autor autor) {
        AutorAdminRequestDto dto = new AutorAdminRequestDto();
        dto.setIdAutor(autor.getIdAutor());
        dto.setNombre(autor.getNombre());
        dto.setSeudonimo(autor.getSeudonimo());
        return dto;
    }

    public static Autor toEntityForAdmin(AutorAdminRequestDto dto, boolean activoActual) {
        Autor autor = new Autor();
        autor.setIdAutor(dto.getIdAutor());
        autor.setNombre(dto.getNombre());
        autor.setSeudonimo(dto.getSeudonimo());
        autor.setActivo(activoActual);
        return autor;
    }
}
