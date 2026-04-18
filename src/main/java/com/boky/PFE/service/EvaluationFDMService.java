package com.boky.PFE.service;

import com.boky.PFE.Beans.SaveEvaluation;
import com.boky.PFE.Beans.SaveEvaluationFDM;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.EvaluationFDM;
import com.boky.PFE.entite.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface EvaluationFDMService
{
    EvaluationFDM AjouterEvaluationFDM(SaveEvaluationFDM model);
    List<EvaluationFDM> AfficherEvaluationFDM();
    List<EvaluationFDM> listeEvaluationFDMByUtilisateur(Long id );

    void SupprimerEvaluationFDM( Long id);
    Utilisateur UtilisateurByEvaluationFDM(Long id);

    Utilisateur FDMByEvaluationFDM(Long id);

    EvaluationFDM ModifierEvaluationFDM(EvaluationFDM evaluation);

    Optional<EvaluationFDM> getEvaluationFDMById(Long id);
    List<EvaluationFDM> listEvaluationFDMByfdm( Long id);
    void supprimerEvaluationFDMParFDM(Long Id);
}
