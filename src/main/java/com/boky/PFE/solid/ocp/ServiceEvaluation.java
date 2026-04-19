package com.boky.PFE.solid.ocp;

import com.boky.PFE.entite.Evaluation;

public class ServiceEvaluation implements EvaluationType {
    
    @Override
    public String getCategorie() {
        return "⭐ Évaluation de service";
    }
    
    @Override
    public void traiterEvaluation(Evaluation evaluation) {
        System.out.println("[OCP] Traitement évaluation service #" + evaluation.getId());
    }
    
    @Override
    public double getCoefficientPonderation() {
        return 1.2; 
    }
    
    @Override
    public String getMessageRecommandation() {
        return "Merci d'avoir évalué notre service !";
    }
}