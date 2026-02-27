package com.comic.hub.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CategoriaAdminRequestDto {

    private Integer idCategoria;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
