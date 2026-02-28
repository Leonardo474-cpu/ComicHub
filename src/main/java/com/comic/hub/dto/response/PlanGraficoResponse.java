package com.comic.hub.dto.response;

public class PlanGraficoResponse {
    
    private String nombrePlan;
    private Long cantidadUsuarios;

    // Constructor con parámetros (necesario para llenarlo desde el Stream en el Service)
    public PlanGraficoResponse(String nombrePlan, Long cantidadUsuarios) {
        this.nombrePlan = nombrePlan;
        this.cantidadUsuarios = cantidadUsuarios;
    }

    // GETTERS (¡Super importantes! JasperReports usa los getters por debajo para leer los fields $F{})
    public String getNombrePlan() {
        return nombrePlan;
    }

    public Long getCantidadUsuarios() {
        return cantidadUsuarios;
    }

    // Setters 
    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public void setCantidadUsuarios(Long cantidadUsuarios) {
        this.cantidadUsuarios = cantidadUsuarios;
    }
}