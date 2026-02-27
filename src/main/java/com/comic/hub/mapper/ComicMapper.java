package com.comic.hub.mapper;

import com.comic.hub.dto.request.ComicAdminRequestDto;
import com.comic.hub.dto.response.ComicListResponseDto;
import com.comic.hub.model.Autor;
import com.comic.hub.model.Categoria;
import com.comic.hub.model.Comic;

public final class ComicMapper {

    private ComicMapper() {
    }

    public static ComicListResponseDto toListResponseDto(Comic comic) {
        ComicListResponseDto dto = new ComicListResponseDto();
        dto.setIdComic(comic.getIdComic());
        dto.setTitulo(comic.getTitulo());
        dto.setEstado(comic.getEstado());
        dto.setActivo(comic.getActivo());
        if (comic.getAutor() != null) {
            dto.setAutor(comic.getAutor().getNombre());
        }
        if (comic.getCategoria() != null) {
            dto.setCategoria(comic.getCategoria().getDescripcion());
        }
        return dto;
    }

    public static ComicAdminRequestDto toAdminRequestDto(Comic comic) {
        ComicAdminRequestDto dto = new ComicAdminRequestDto();
        dto.setIdComic(comic.getIdComic());
        dto.setTitulo(comic.getTitulo());
        dto.setSinopsis(comic.getSinopsis());
        dto.setEstado(comic.getEstado());
        dto.setRutaImagenPortada(comic.getRutaImagenPortada());
        if (comic.getAutor() != null) {
            dto.setAutorId(comic.getAutor().getIdAutor());
        }
        if (comic.getCategoria() != null) {
            dto.setCategoriaId(comic.getCategoria().getIdCategoria());
        }
        return dto;
    }

    public static Comic toEntityForAdmin(ComicAdminRequestDto dto, Autor autor, Categoria categoria, boolean activoActual) {
        Comic comic = new Comic();
        comic.setIdComic(dto.getIdComic());
        comic.setTitulo(dto.getTitulo());
        comic.setSinopsis(dto.getSinopsis());
        comic.setAutor(autor);
        comic.setCategoria(categoria);
        comic.setEstado(dto.getEstado());
        comic.setActivo(activoActual);
        return comic;
    }
}
