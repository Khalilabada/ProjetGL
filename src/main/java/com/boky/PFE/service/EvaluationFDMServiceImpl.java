package com.boky.PFE.service;

import com.boky.PFE.Beans.SaveEvaluationFDM;
import com.boky.PFE.entite.EvaluationFDM;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.repository.EvaluationFDMRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class EvaluationFDMServiceImpl implements EvaluationFDMService {

    private final EvaluationFDMRepository evaluationFDMRepository;
    private final UtilisateurService utilisateurService;
    private final NotificationService notificationService;

    public EvaluationFDMServiceImpl(
            EvaluationFDMRepository evaluationFDMRepository,
            UtilisateurService utilisateurService,
            NotificationService notificationService) {
        this.evaluationFDMRepository = evaluationFDMRepository;
        this.utilisateurService = utilisateurService;
        this.notificationService = notificationService;
    }
    @Override
    public EvaluationFDM AjouterEvaluationFDM(SaveEvaluationFDM model){
        EvaluationFDM evaluation = SaveEvaluationFDM.toEntity(model);
        Optional<Utilisateur> fdm = utilisateurService.getUtilisateurById(model.getId_FDM());
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(model.getId_utilisateur());

        if (fdm.isPresent() && utilisateur.isPresent()) {

            evaluation.setFdm(fdm.get());
            evaluation.setUtilisateur(utilisateur.get());
            notificationService.notifyEvaluationFDMAdded(fdm.get());

            return evaluationFDMRepository.save(evaluation);}
        else{
            return null;}

    }

    @Override
    public List<EvaluationFDM> AfficherEvaluationFDM() {
        return evaluationFDMRepository.findAll();
    }

    @Override
    public     List<EvaluationFDM> listeEvaluationFDMByUtilisateur(Long id ) {
        return evaluationFDMRepository.findByutilisateurId(id);
    }

    @Override
    public Utilisateur UtilisateurByEvaluationFDM(Long id) {
        Optional<EvaluationFDM> evaluation =  evaluationFDMRepository.findById(id);
        return evaluation.get().getUtilisateur();
    }
    @Override
    public Utilisateur FDMByEvaluationFDM(Long id) {
        Optional<EvaluationFDM> evaluation =  evaluationFDMRepository.findById(id);
        return evaluation.get().getFdm();
    }

    @Override
    public EvaluationFDM ModifierEvaluationFDM(EvaluationFDM evaluation) {

        Utilisateur utilisateur = this.UtilisateurByEvaluationFDM(evaluation.getId());
        Utilisateur fdm = this.FDMByEvaluationFDM(evaluation.getId());
        evaluation.setUtilisateur(utilisateur);
        evaluation.setFdm(fdm);
        return evaluationFDMRepository.save(evaluation);
    }
    @Override
    public Optional<EvaluationFDM> getEvaluationFDMById(Long id) {
        return evaluationFDMRepository.findById(id);
    }
    @Override
    public void SupprimerEvaluationFDM( Long id) {
        evaluationFDMRepository.deleteById(id);
    }
    @Override
    public List<EvaluationFDM> listEvaluationFDMByfdm( Long id) {
        return evaluationFDMRepository.findByfdmId(id);
    }
    @Override
    @Transactional
    public void supprimerEvaluationFDMParFDM(Long Id) {
        List<EvaluationFDM> evaluations = evaluationFDMRepository.findByfdmId(Id);
        for (EvaluationFDM evaluation : evaluations) {
            evaluationFDMRepository.delete(evaluation);
        }
    }
}
