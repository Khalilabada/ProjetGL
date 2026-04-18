package com.boky.PFE.service;

import com.boky.PFE.Beans.SavePlanification;
import com.boky.PFE.entite.Planification;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

public interface PlanificationService
{
    Planification AjouterPlanification (SavePlanification model);
    Planification ModifierPlanification(Planification planification);
    List<Planification> AfficherPlanification();
    void SupprimerPlanification (Long id);
    Optional<Planification> getPlanificationById(Long id);

    List<Planification> listePlanificationByFdm(Long id);

    Utilisateur FdmByPlanning(  Long id);

}
