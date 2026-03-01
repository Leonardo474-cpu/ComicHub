package com.comic.hub.service;

import com.comic.hub.dto.request.CategoriaAdminRequestDto;
import com.comic.hub.dto.response.CategoriaListResponseDto;
import org.springframework.data.domain.Page;

public interface CategoriaService {

    Page<CategoriaListResponseDto> listarTodos(String estado, String q, int page, int size);

    CategoriaAdminRequestDto buscarPorIdParaEdicion(Integer idCategoria);

    void guardarDesdeAdmin(CategoriaAdminRequestDto categoriaAdminRequestDto);

    void cambiarEstado(Integer idCategoria);
}
