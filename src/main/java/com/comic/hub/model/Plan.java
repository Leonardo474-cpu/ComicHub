package com.comic.hub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "tb_plan")

//Lombook
@Getter
@Setter
@NoArgsConstructor

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

}
