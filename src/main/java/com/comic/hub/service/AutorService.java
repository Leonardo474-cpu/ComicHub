package com.comic.hub.service;

import com.comic.hub.dto.request.AutorAdminRequestDto;
import com.comic.hub.dto.response.AutorListResponseDto;

import java.util.List;

public interface AutorService {

    List<AutorListResponseDto> listarTodos();

    AutorAdminRequestDto buscarPorIdParaEdicion(Integer idAutor);

    void guardarDesdeAdmin(AutorAdminRequestDto autorAdminRequestDto);

    void cambiarEstado(Integer idAutor);
}
