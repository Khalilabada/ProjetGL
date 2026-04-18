package com.boky.PFE.restController;

import com.boky.PFE.entite.Planification;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.service.PlanificationService;
import com.boky.PFE.service.ReservationFMServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Planification")
public class PlanificationRestController {

    @Autowired
    PlanificationService planificationService;
    
    @Autowired
    ReservationFMServiceImpl reservationFMService;

    @RequestMapping(method = RequestMethod.POST)
    public Planification AjouterPlanification(@RequestBody com.boky.PFE.Beans.SavePlanification model) {
        System.out.println("[PlanificationRestController] Création d'un planning");
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
    public Planification ModifierPlanification(@PathVariable("id") Long id, @RequestBody Planification planification) {
        Planification newPlanification = planificationService.ModifierPlanification(planification);
        return newPlanification;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Planification> getPlanificationById(@PathVariable("id") long id) {
        Optional<Planification> planification = planificationService.getPlanificationById(id);
        return planification;
    }

    @RequestMapping("get-all-by-id-FDM/{id}")
    public List<Planification> listePlanificationByFdm(@PathVariable Long id) {
        return planificationService.listePlanificationByFdm(id);
    }

    @RequestMapping("get-utilisateur/{id}")
    public Utilisateur FdmByPlanning(@PathVariable Long id) {
        return planificationService.FdmByPlanning(id);
    }
    
    // ========== NOUVELLES MÉTHODES POUR LE PATTERN STATE ==========
    
    /**
     * Démarrer le travail pour un planning (changement d'état RÉSERVÉ → OCCUPÉ)
     */
    @PutMapping("/demarrer/{id}")
    public String demarrerTravail(@PathVariable("id") Long id) {
        System.out.println("[PlanificationRestController] Demande de démarrage du travail pour planning #" + id);
        reservationFMService.demarrerTravail(id);
        return "Travail démarré pour le planning #" + id;
    }
    
    /**
     * Terminer le travail pour un planning (changement d'état OCCUPÉ → TERMINÉ)
     */
    @PutMapping("/terminer/{id}")
    public String terminerTravail(@PathVariable("id") Long id) {
        System.out.println("[PlanificationRestController] Demande de fin du travail pour planning #" + id);
        reservationFMService.terminerTravail(id);
        return "Travail terminé pour le planning #" + id;
    }
    
    /**
     * Annuler un planning (changement d'état vers ANNULE)
     */
    @PutMapping("/annuler/{id}")
    public String annulerPlanning(@PathVariable("id") Long id) {
        System.out.println("[PlanificationRestController] Demande d'annulation du planning #" + id);
        reservationFMService.annulerPlanning(id);
        return "Planning #" + id + " annulé";
    }
    
    /**
     * Obtenir l'état actuel d'un planning
     */
    @GetMapping("/etat/{id}")
    public String getEtatPlanning(@PathVariable("id") Long id) {
        Optional<Planification> planningOpt = planificationService.getPlanificationById(id);
        if (planningOpt.isPresent()) {
            Planification planning = planningOpt.get();
            return "Planning #" + id + " est en état: " + planning.getNomEtat();
        }
        return "Planning non trouvé";
    }
}