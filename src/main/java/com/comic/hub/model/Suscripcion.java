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

	public Suscripcion() {}

	public int getId_suscripcion() {
		return id_suscripcion;
	}

	public void setId_suscripcion(int id_suscripcion) {
		this.id_suscripcion = id_suscripcion;
	}

	public LocalDate getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(LocalDate fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public LocalDate getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(LocalDate fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public int getId_plan() {
		return id_plan;
	}

	public void setId_plan(int id_plan) {
		this.id_plan = id_plan;
	}

}
