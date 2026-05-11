package com.boky.PFE.solid.ocp;

import org.springframework.stereotype.Component;

@Component
public class EvaluationTypeFactory {
    
    public EvaluationType getType(String commentaire) {
        if (commentaire == null) {
            return new LogementEvaluation();
        }
        
        String commentaireLower = commentaire.toLowerCase();
        
        if (commentaireLower.contains("service") || 
            commentaireLower.contains("accueil") ||
            commentaireLower.contains("support")) {
            return new ServiceEvaluation();
        }
        
        return new LogementEvaluation();
    }
}