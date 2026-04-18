package com.boky.PFE.service;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.Beans.SaveEvaluation;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.repository.EvaluationRepositrory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
public class EvaluationServiceImpl implements EvaluationService
{
    @Autowired
    EvaluationRepositrory evaluationRepositrory;
    @Autowired
    AnnonceService annonceService;
    @Autowired
    UtilisateurService utilisateurService;
    @Autowired
    EmailService emailService;
    @Override
    public Evaluation AjouterEvaluation(SaveEvaluation model){
        Evaluation evaluation = SaveEvaluation.toEntity(model);
        Optional<Annonce> annonce = annonceService.getAnnonceById(model.getId_annonce());
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(model.getId_client());
        Utilisateur annonceur = annonceService.UtilisateurByAnnonceur(annonce.get().getId());

        if (annonce.isPresent() && utilisateur.isPresent()) {

            evaluation.setAnnonce(annonce.get());
            evaluation.setUtilisateur(utilisateur.get());
            emailService.SendSimpleMessage(
                    annonceur.getEmail(),
                    "Nouveau commentaire sur votre annonce",
                    "Bonjour,\n\n" +
                            "Nous vous informons qu'un nouveau commentaire a été laissé sur votre annonce \"" + annonce.get().getTitre() + "\". " +
                            "Veuillez consulter votre profil pour lire et répondre au commentaire.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe de gestion des annonces"
            );

            return evaluationRepositrory.save(evaluation);}
        else{
            return null;}

    }

    @Override
    public List<Evaluation> AfficherEvaluation() {
        return evaluationRepositrory.findAll();
    }

    @Override
    public List<Evaluation> listeEvaluationByUtilisateur(Long id ) {
        return evaluationRepositrory.findByutilisateurId(id);
    }

    @Override
    public Utilisateur ClientByEvaluation(Long id) {
        Optional<Evaluation> evaluation =  evaluationRepositrory.findById(id);
        return evaluation.get().getUtilisateur();
    }
    @Override
    public Annonce AnnonceByEvaluation(Long id) {
        Optional<Evaluation> evaluation =  evaluationRepositrory.findById(id);
        return evaluation.get().getAnnonce();
    }

    @Override
    public Evaluation ModifierEvaluation(Evaluation evaluation) {

        Utilisateur client = this.ClientByEvaluation(evaluation.getId());
        Annonce annonce = this.AnnonceByEvaluation(evaluation.getId());
        evaluation.setUtilisateur(client);
        evaluation.setAnnonce(annonce);
        return evaluationRepositrory.save(evaluation);
    }
    @Override
    public Optional<Evaluation> getEvaluationById(Long id) {
        return evaluationRepositrory.findById(id);
    }
    @Override
    public void SupprimerEvaluation(Long id) {
        evaluationRepositrory.deleteById(id);
    }
    @Override
    public List<Evaluation> listEvaluationByAnnonce( Long id) {
        return evaluationRepositrory.findByannonceId(id);
    }
    @Override
    @Transactional
    public void supprimerEvaluationsParAnnonce(Long annonceId) {
        List<Evaluation> evaluations = evaluationRepositrory.findByannonceId(annonceId);
        for (Evaluation evaluation : evaluations) {
            evaluationRepositrory.delete(evaluation);
        }
    }

}
