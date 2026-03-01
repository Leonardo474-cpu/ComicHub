package com.comic.hub.service;

import org.springframework.data.domain.Page;

import com.comic.hub.dto.request.UsuarioAdminRequestDto;
import com.comic.hub.dto.request.UsuarioRegistroRequestDto;
import com.comic.hub.dto.response.UsuarioListResponseDto;
import com.comic.hub.model.Usuario;

public interface UsuarioService {

    Page<UsuarioListResponseDto> listarTodos(String estado, String q, int page, int size);

    UsuarioAdminRequestDto buscarPorIdParaEdicion(Integer id);

    void cambiarEstado(Integer id);

    Usuario login(String correo, String password);

    void registrar(UsuarioRegistroRequestDto usuarioRegistroRequestDto);

    void guardarDesdeAdmin(UsuarioAdminRequestDto usuarioAdminRequestDto);
}
