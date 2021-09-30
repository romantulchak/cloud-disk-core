package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Plan;
import com.romantulchak.clouddisk.model.enums.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findPlanByName(PlanType planType);

}
