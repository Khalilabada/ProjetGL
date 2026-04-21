package com.boky.PFE.restController;

import com.boky.PFE.Beans.SavePlanification;
import com.boky.PFE.entite.Planification;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.service.PlanificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Planification")
public class PlanificationRestController {

    @Autowired
    private PlanificationService planificationService;

    // PlanificationRepository SUPPRIMÉ — n'appartient pas à cette couche

    @RequestMapping(method = RequestMethod.POST)
    public Planification AjouterPlanification(@RequestBody SavePlanification model) {
        return planificationService.AjouterPlanification(model);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Planification> AfficherPlanification() {
        return planificationService.AfficherPlanification();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void SupprimerPlanification(@PathVariable("id") Long id) {
        planificationService.SupprimerPlanification(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Planification ModifierPlanification(@PathVariable("id") Long id,
                                               @RequestBody Planification planification) {
        return planificationService.ModifierPlanification(planification);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Planification> getPlanificationById(@PathVariable("id") long id) {
        return planificationService.getPlanificationById(id);
    }

    @RequestMapping("get-all-by-id-FDM/{id}")
    public List<Planification> listePlanificationByFdm(@PathVariable Long id) {
        return planificationService.listePlanificationByFdm(id);
    }

    @RequestMapping("get-utilisateur/{id}")
    public Utilisateur FdmByPlanning(@PathVariable Long id) {
        return planificationService.FdmByPlanning(id);
    }
}