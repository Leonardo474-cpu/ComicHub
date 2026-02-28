package com.comic.hub.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comic.hub.model.Plan;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    Optional<Plan> findByNombrePlan(String nombrePlan);

    List<Plan> findByActivoTrueOrderByPrecioAsc();
}
