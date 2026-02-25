package com.comic.hub.service;

import java.util.List;

import com.comic.hub.dto.request.UsuarioAdminRequestDto;
import com.comic.hub.dto.request.UsuarioRegistroRequestDto;
import com.comic.hub.dto.response.UsuarioListResponseDto;
import com.comic.hub.model.Usuario;

public interface UsuarioService {

    List<UsuarioListResponseDto> listarTodos();

    UsuarioAdminRequestDto buscarPorIdParaEdicion(Integer id);

    void eliminar(Integer id);

    Usuario login(String correo, String password);

    void registrar(UsuarioRegistroRequestDto usuarioRegistroRequestDto);

    void guardarDesdeAdmin(UsuarioAdminRequestDto usuarioAdminRequestDto);
}
