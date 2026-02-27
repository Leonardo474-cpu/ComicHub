package com.comic.hub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.comic.hub.model.converter.BooleanToIntegerConverter;

@Entity
@Table(name = "tb_plan")
public class Plan {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_plan")
	private Integer idPlan;

	@Column(name = "nombre_plan")
	private String nombrePlan;

	@Column(name = "precio")
	private Double precio;

	@Column(name = "dias_duracion", nullable = false)
	private Integer diasDuracion;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    @Convert(converter = BooleanToIntegerConverter.class)
    private boolean activo = true;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
