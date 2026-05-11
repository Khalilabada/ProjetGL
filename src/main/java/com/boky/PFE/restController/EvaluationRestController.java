package com.boky.PFE.restController;

import com.boky.PFE.Beans.SaveEvaluation;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Evaluation")
public class EvaluationRestController {
    
    @Autowired
    EvaluationService evaluationService;
    
    @RequestMapping(method = RequestMethod.POST)
    public Evaluation ajouterEvaluation(@RequestBody SaveEvaluation model) {
        Evaluation evaluation = evaluationService.AjouterEvaluation(model);
        
        if (evaluation != null) {
            System.out.println("=========================================");
            System.out.println("📝 NOUVELLE ÉVALUATION CRÉÉE");
            System.out.println("   ID: " + evaluation.getId());
            System.out.println("   Catégorie: " + evaluation.getCategorieEvaluation());
            System.out.println("   Coefficient: " + evaluation.getCoefficientPonderation());
            System.out.println("   Message: " + evaluation.getMessageRecommandation());
            System.out.println("=========================================");
            
            evaluation.traiterEvaluation();
        }
        
        return evaluation;
    }
    
    @RequestMapping("get-all-by-id-utilisateur/{id}")
    public List<Evaluation> listEvaluationByUtilisateur(@PathVariable Long id) {
        return evaluationService.listeEvaluationByUtilisateur(id);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Evaluation> getEvaluationById(@PathVariable("id") long id) {
        Optional<Evaluation> evaluation = evaluationService.getEvaluationById(id);
        return evaluation;
    }
    
    @RequestMapping("get-client/{id}")
    public Utilisateur ClientByEvaluation(@PathVariable Long id) {
        return evaluationService.ClientByEvaluation(id);
    }
    
    @RequestMapping("get-annonce/{id}")
    public Annonce AnnonceByEvaluation(@PathVariable Long id) {
        return evaluationService.AnnonceByEvaluation(id);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Evaluation ModifierEvaluation(@PathVariable("id") Long id, @RequestBody Evaluation evaluation) {
        Evaluation newEvaluation = evaluationService.ModifierEvaluation(evaluation);
        return newEvaluation;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public List<Evaluation> AfficherEvaluation() {
        return evaluationService.AfficherEvaluation();
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void SupprimerEvaluation(@PathVariable("id") Long id) {
        evaluationService.SupprimerEvaluation(id);
    }
    
    @RequestMapping("get-all-by-id-annonce/{id}")
    public List<Evaluation> listEvaluationByAnnonce(@PathVariable Long id) {
        return evaluationService.listEvaluationByAnnonce(id);
    }
    
    @RequestMapping(value = "/supprimer-par-annonce/{annonceId}", method = RequestMethod.DELETE)
    public void supprimerEvaluationsParAnnonce(@PathVariable("annonceId") Long annonceId) {
        evaluationService.supprimerEvaluationsParAnnonce(annonceId);
    }
    
    
    @GetMapping("/categorie/{id}")
    public String getEvaluationCategorie(@PathVariable("id") Long id) {
        Optional<Evaluation> evaluation = evaluationService.getEvaluationById(id);
        if (evaluation.isPresent()) {
            Evaluation e = evaluation.get();
            return "Évaluation #" + id + " : " + e.getCategorieEvaluation() + 
                   " - Coefficient: " + e.getCoefficientPonderation();
        }
        return "Évaluation non trouvée";
    }
}