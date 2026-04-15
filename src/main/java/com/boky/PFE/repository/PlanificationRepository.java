package com.boky.PFE.repository;

import com.boky.PFE.entite.Planification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanificationRepository extends JpaRepository<Planification,Long> {



    List<Planification> findByFdmId(Long id);
}
