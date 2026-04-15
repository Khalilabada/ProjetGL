package com.boky.PFE.restController;

import com.boky.PFE.Beans.SaveEvaluation;
import com.boky.PFE.Beans.SaveEvaluationFDM;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.EvaluationFDM;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.service.EvaluationFDMService;
import com.boky.PFE.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/EvaluationFDM")
public class EvaluationFDMRestController
{
    @Autowired
    EvaluationFDMService evaluationFDMService;
    @RequestMapping(method = RequestMethod.POST)
    public EvaluationFDM AjouterEvaluationFDM(@RequestBody SaveEvaluationFDM model){

        return evaluationFDMService.AjouterEvaluationFDM(model);
    }
    @RequestMapping("get-all-by-id-utilisateur/{id}")
    public List<EvaluationFDM> listeEvaluationFDMByUtilisateur(@PathVariable Long id){
        return evaluationFDMService.listeEvaluationFDMByUtilisateur(id);
    }
    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    public Optional<EvaluationFDM> getEvaluationFDMById(@PathVariable("id") long id){

        Optional<EvaluationFDM> evaluation = evaluationFDMService.getEvaluationFDMById(id);
        return evaluation;
    }
    @RequestMapping("get-utilisateur/{id}")
    public Utilisateur UtilisateurByEvaluationFDM(@PathVariable  Long id) {
        return evaluationFDMService.UtilisateurByEvaluationFDM(id);
    }
    @RequestMapping("get-fdm/{id}")
    public Utilisateur FDMByEvaluation(@PathVariable  Long id) {
        return evaluationFDMService.FDMByEvaluationFDM(id);
    }
    @RequestMapping(value = "/{id}" ,method = RequestMethod.PUT)
    public EvaluationFDM ModifierEvaluationFDM(@PathVariable("id")Long id, @RequestBody EvaluationFDM evaluation){
        EvaluationFDM newEvaluation = evaluationFDMService.ModifierEvaluationFDM(evaluation);
        return newEvaluation;
    }
    @RequestMapping(method = RequestMethod.GET)
    public List<EvaluationFDM> AfficherEvaluationFDM()
    {
        return evaluationFDMService.AfficherEvaluationFDM();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE )

    public void SupprimerEvaluationFDM(@PathVariable("id") Long id){
        evaluationFDMService.SupprimerEvaluationFDM(id);

    }
    @RequestMapping("get-all-by-id-fdm/{id}")
    public List<EvaluationFDM> listEvaluationFDMByfdm(@PathVariable Long id){
        return evaluationFDMService.listEvaluationFDMByfdm(id);
    }
    @RequestMapping(value = "/supprimer-par-fdm/{fdmId}", method = RequestMethod.DELETE)
    public void supprimerEvaluationFDMParFDM(@PathVariable("annonceId") Long fdmId) {
        evaluationFDMService.supprimerEvaluationFDMParFDM(fdmId);
    }
}
