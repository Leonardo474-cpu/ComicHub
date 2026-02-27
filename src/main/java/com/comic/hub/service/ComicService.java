package com.comic.hub.service;

import com.comic.hub.dto.request.ComicAdminRequestDto;
import com.comic.hub.dto.response.ComicListResponseDto;
import com.comic.hub.model.Autor;
import com.comic.hub.model.Categoria;
import com.comic.hub.model.Comic;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ComicService {

    List<ComicListResponseDto> listarTodos();

    Comic obtenerDetalle(Integer idComic);

    ComicAdminRequestDto buscarPorIdParaEdicion(Integer idComic);

    void guardarDesdeAdmin(ComicAdminRequestDto comicAdminRequestDto, MultipartFile portada);

    void cambiarEstado(Integer idComic);

    List<Autor> listarAutoresActivos();

    List<Categoria> listarCategoriasActivas();
}
