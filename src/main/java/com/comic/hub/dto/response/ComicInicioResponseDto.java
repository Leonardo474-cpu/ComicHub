package com.comic.hub.dto.response;

public class ComicInicioResponseDto {

    private Integer idComic;
    private String titulo;
    private String rutaImagenPortada;
    private String autor;
    private String sinopsis;
    private boolean activo;

    public ComicInicioResponseDto(Integer idComic,
                                  String titulo,
                                  String rutaImagenPortada,
                                  String autor,
                                  String sinopsis,
                                  boolean activo) {
        this.idComic = idComic;
        this.titulo = titulo;
        this.rutaImagenPortada = rutaImagenPortada;
        this.autor = autor;
        this.sinopsis = sinopsis;
        this.activo = activo;
    }

    public Integer getIdComic() {
        return idComic;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getRutaImagenPortada() {
        return rutaImagenPortada;
    }

    public String getAutor() {
        return autor;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public boolean isActivo() {
        return activo;
    }
}
