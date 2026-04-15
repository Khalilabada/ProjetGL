package com.boky.PFE.repository;

import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.EvaluationFDM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationFDMRepository extends JpaRepository<EvaluationFDM,Long>
{


    List<EvaluationFDM> findByfdmId(Long id);

    List<EvaluationFDM> findByutilisateurId(Long id);
}
