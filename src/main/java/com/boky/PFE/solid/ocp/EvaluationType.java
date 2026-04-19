package com.boky.PFE.solid.ocp;

import com.boky.PFE.entite.Evaluation;

public interface EvaluationType {
    
    String getCategorie();
    
    void traiterEvaluation(Evaluation evaluation);
    
    double getCoefficientPonderation();
    
    String getMessageRecommandation();
}