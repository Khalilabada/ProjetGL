package com.boky.PFE.service;

import com.boky.PFE.Beans.SaveEvaluation;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Annonceur;
import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.repository.EvaluationRepositrory;
import com.boky.PFE.repository.ReservationRepository;
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
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    OclEvaluationValidator oclEvaluationValidator;

    @Override
    public Evaluation AjouterEvaluation(SaveEvaluation model){
        Evaluation evaluation = SaveEvaluation.toEntity(model);

        Annonce annonce = annonceService.getAnnonceById(model.getId_annonce())
                .orElseThrow(() -> new NoSuchElementException("Annonce non trouvée avec l'id: " + model.getId_annonce()));

        Utilisateur utilisateur = utilisateurService.getUtilisateurById(model.getId_client())
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé avec l'id: " + model.getId_client()));

        Annonceur annonceur = annonceService.UtilisateurByAnnonceur(annonce.getId());

        evaluation.setAnnonce(annonce);
        evaluation.setUtilisateur(utilisateur);

        List<Reservation> reservationsAnnonce = reservationRepository.findByAnnonceId(annonce.getId());
        List<Evaluation> evaluationsAnnonce = evaluationRepositrory.findByannonceId(annonce.getId());
 oclEvaluationValidator.validateBeforeSave(
    evaluation,
    annonce,
    annonceur,
    reservationsAnnonce,
    evaluationsAnnonce
);
   


        emailService.SendSimpleMessage(
                annonceur.getEmail(),
                "Nouveau commentaire sur votre annonce",
                "Bonjour,\n\n" +
                        "Nous vous informons qu'un nouveau commentaire a été laissé sur votre annonce \"" + annonce.getTitre() + "\". " +
                        "Veuillez consulter votre profil pour lire et répondre au commentaire.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe de gestion des annonces"
        );

        return evaluationRepositrory.save(evaluation);
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
        Evaluation evaluation = evaluationRepositrory.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evaluation non trouvée avec l'id: " + id));
        return evaluation.getUtilisateur();
    }
    @Override
    public Annonce AnnonceByEvaluation(Long id) {
        Evaluation evaluation = evaluationRepositrory.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evaluation non trouvée avec l'id: " + id));
        return evaluation.getAnnonce();
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
 