package com.boky.PFE.repository;

import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Evaluation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepositrory extends JpaRepository<Evaluation,Long> {
    List<Evaluation> findByutilisateurId(Long id);

    List<Evaluation> findByannonceId(Long id);


}
