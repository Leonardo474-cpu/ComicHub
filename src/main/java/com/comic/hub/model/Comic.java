package com.comic.hub.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_comic")

@Getter
@Setter
@NoArgsConstructor
public class Comic {

	
	@Id
	@GeneratedValue(strategy =   GenerationType.IDENTITY)
	@Column(name = "id_comic")
	private Integer idComic;
	
	@Column(name = "titulo")
	private String titulo;
	
	@Column(name = "sinopsis")
	private String sinopsis;
	
	@Column(name = "autor")
	private String autor;
	
	@Column(name = "ruta_imagen")
	private String  rutaImagen;
	
	@Column(name = "ruta_archivo")
	private String rutaArchivo;
	
	
	@ManyToOne
    @JoinColumn(name = "id_categoria")
	private Categoria Categoria;
	
}

