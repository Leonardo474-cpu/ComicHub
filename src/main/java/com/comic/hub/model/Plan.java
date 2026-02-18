package com.comic.hub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "tb_plan")
public class Plan {
	
	
	@Id
	@GeneratedValue(strategy =   GenerationType.IDENTITY)
	@Column(name = "id_plan")
	private Integer idPlan;
	
	@Column(name = " nombre_plan")
	private String nombrePlan;
	
	@Column(name = " precio")
	private Double precio;
	
	@Column(name = " dias_duracion")
	private Integer diasDuracion;

	public Plan() {}

	public Integer getIdPlan() {
		return idPlan;
	}

	public void setIdPlan(Integer idPlan) {
		this.idPlan = idPlan;
	}

	public String getNombrePlan() {
		return nombrePlan;
	}

	public void setNombrePlan(String nombrePlan) {
		this.nombrePlan = nombrePlan;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getDiasDuracion() {
		return diasDuracion;
	}

	public void setDiasDuracion(Integer diasDuracion) {
		this.diasDuracion = diasDuracion;
	}

	
}
