package com.boky.PFE.state;


import com.boky.PFE.entite.Planification;

public class OccupeState implements PlanningState {

    @Override
    public void reserver(Planification planning) {
        System.out.println("[State] Impossible de réserver : planning déjà occupé !");
    }

    @Override
    public void commencer(Planification planning) {
        System.out.println("[State] Travail déjà commencé !");
    }

    @Override
    public void terminer(Planification planning) {
        System.out.println("[State] Planning #" + planning.getId() + " : OCCUPÉ → TERMINÉ");
        planning.setEtatCourant(new TermineState());
    }

    @Override
    public void annuler(Planification planning) {
        System.out.println("[State] Planning #" + planning.getId() + " : OCCUPÉ → ANNULE");
        planning.setEtatCourant(new AnnuleState());
    }

    @Override
    public String getNomEtat() {
        return "OCCUPÉ";
    }

    @Override
    public boolean peutReserver() {
        return false;
    }

    @Override
    public boolean peutCommencer() {
        return false;
    }

    @Override
    public boolean peutTerminer() {
        return true;
    }

    @Override
    public boolean peutAnnuler() {
        return true;
    }
}