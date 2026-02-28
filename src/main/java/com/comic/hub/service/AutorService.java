package com.comic.hub.service;

import com.comic.hub.dto.request.AutorAdminRequestDto;
import com.comic.hub.dto.response.AutorListResponseDto;
import org.springframework.data.domain.Page;

public interface AutorService {

    Page<AutorListResponseDto> listarTodos(String estado, String q, int page, int size);

    AutorAdminRequestDto buscarPorIdParaEdicion(Integer idAutor);

    void guardarDesdeAdmin(AutorAdminRequestDto autorAdminRequestDto);

    void cambiarEstado(Integer idAutor);
}
