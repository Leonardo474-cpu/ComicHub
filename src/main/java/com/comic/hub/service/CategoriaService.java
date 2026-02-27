package com.comic.hub.service;

import com.comic.hub.dto.request.CategoriaAdminRequestDto;
import com.comic.hub.dto.response.CategoriaListResponseDto;

import java.util.List;

public interface CategoriaService {

    List<CategoriaListResponseDto> listarTodos();

    CategoriaAdminRequestDto buscarPorIdParaEdicion(Integer idCategoria);

    void guardarDesdeAdmin(CategoriaAdminRequestDto categoriaAdminRequestDto);

    void cambiarEstado(Integer idCategoria);
}
