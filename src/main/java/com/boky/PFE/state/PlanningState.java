package com.boky.PFE.state;

import com.boky.PFE.entite.Planification;

public interface PlanningState {
    
    // Actions possibles sur un planning
    void reserver(Planification planning);
    void commencer(Planification planning);
    void terminer(Planification planning);
    void annuler(Planification planning);
    
    // Pour savoir dans quel état on est
    String getNomEtat();
    
    // Vérifier si une action est possible
    boolean peutReserver();
    boolean peutCommencer();
    boolean peutTerminer();
    boolean peutAnnuler();
}