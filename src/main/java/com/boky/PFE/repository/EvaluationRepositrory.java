package com.boky.PFE.repository;

import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepositrory extends JpaRepository<Evaluation,Long> {
    List<Evaluation> findByannonceId(Long id);

    List<Evaluation> findByutilisateurId(Long id);
}
