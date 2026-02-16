package com.comic.hub.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tb_suscripcion")
public class Suscripcion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_suscripcion")
	private int id_suscripcion;
	
	@Column(name = "fecha_inicio")
	private LocalDate fecha_inicio;
	
	@Column(name = "fecha_fin")
	private LocalDate fecha_fin;
	
	@Column(name = "estado")
	private String estado;
	
	@Column(name = "id_usuario")
	private int id_usuario;
	
	@Column(name = "id_plan")
	private int id_plan;

}
