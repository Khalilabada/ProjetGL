package com.boky.PFE.service;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.Beans.SaveEvaluation;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface EvaluationService
{
    Evaluation AjouterEvaluation(SaveEvaluation model);
    List<Evaluation> AfficherEvaluation();
    List<Evaluation> listeEvaluationByUtilisateur(Long id );

    void SupprimerEvaluation( Long id);
    Utilisateur ClientByEvaluation(Long id);

    Annonce AnnonceByEvaluation(Long id);

    Evaluation ModifierEvaluation(Evaluation evaluation);

    Optional<Evaluation> getEvaluationById(Long id);
    List<Evaluation> listEvaluationByAnnonce( Long id);
    void supprimerEvaluationsParAnnonce(Long annonceId);
}
