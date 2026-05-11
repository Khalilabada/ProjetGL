package com.boky.PFE.solid.ocp;

import com.boky.PFE.entite.Evaluation;

public class LogementEvaluation implements EvaluationType {
    
    @Override
    public String getCategorie() {
        return "🏠 Évaluation de logement";
    }
    
    @Override
    public void traiterEvaluation(Evaluation evaluation) {
        System.out.println("[OCP] Traitement évaluation logement #" + evaluation.getId());
    }
    
    @Override
    public double getCoefficientPonderation() {
        return 1.0; 
    }
    
    @Override
    public String getMessageRecommandation() {
        return "Merci d'avoir partagé votre expérience sur ce logement !";
    }
}