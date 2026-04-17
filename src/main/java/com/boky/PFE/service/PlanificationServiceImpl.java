package com.boky.PFE.service;

import com.boky.PFE.Beans.SavePlanification;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Planification;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.factory.ServiceFactory;
import com.boky.PFE.factory.offre.Offre;
import com.boky.PFE.repository.PlanificationRepository;
import com.boky.PFE.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PlanificationServiceImpl extends BaseService implements PlanificationService {


    @Autowired
    PlanificationRepository planificationRepository;
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Override
    public Planification AjouterPlanification(SavePlanification model) {
        Offre offre = createOffre(model.getType());

        offre.remplirDepuisPlanification(model);
        Planification planification = (Planification) offre;

        System.out.println("idFDM" + model.getId_fdm());
        Utilisateur utilisateur = utilisateurRepository.findById(model.getId_fdm()).get();
        planification.setFdm(utilisateur);
        return planificationRepository.save(planification);
    }

    @Override
    public Planification ModifierPlanification(Planification planification) {
        Utilisateur fmd = this.FdmByPlanning(planification.getId());
        planification.setFdm(fmd);
        return planificationRepository.save(planification);
    }

    @Override
    public List<Planification> AfficherPlanification() {
        return planificationRepository.findAll();
    }

    @Override
    public void SupprimerPlanification(Long id) {
        planificationRepository.deleteById(id);
    }

    @Override
    public Optional<Planification> getPlanificationById(Long id) {
        return planificationRepository.findById(id);
    }

    @Override
    public List<Planification> listePlanificationByFdm(Long id) {
        return planificationRepository.findByFdmId(id);
    }

    @Override
    public Utilisateur FdmByPlanning(Long id) {
        Optional<Planification> planification = planificationRepository.findById(id);
        return planification.get().getFdm();
    }
}
