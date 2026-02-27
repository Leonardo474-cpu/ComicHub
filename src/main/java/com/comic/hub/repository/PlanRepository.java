package com.comic.hub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comic.hub.model.Plan;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    Optional<Plan> findByNombrePlan(String nombrePlan);
}
