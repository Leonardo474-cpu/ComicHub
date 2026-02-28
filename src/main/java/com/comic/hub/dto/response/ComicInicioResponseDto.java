package com.comic.hub.dto.response;

public class ComicInicioResponseDto {

    private Integer idComic;
    private String titulo;
    private String rutaImagenPortada;
    private String autor;
    private String sinopsis;

    public ComicInicioResponseDto(Integer idComic, String titulo, String rutaImagenPortada, String autor, String sinopsis) {
        this.idComic = idComic;
        this.titulo = titulo;
        this.rutaImagenPortada = rutaImagenPortada;
        this.autor = autor;
        this.sinopsis = sinopsis;
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
}
