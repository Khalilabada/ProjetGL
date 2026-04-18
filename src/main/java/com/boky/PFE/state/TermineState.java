package com.boky.PFE.state;


import com.boky.PFE.entite.Planification;

public class TermineState implements PlanningState {

    @Override
    public void reserver(Planification planning) {
        System.out.println("[State] Impossible : planning déjà terminé !");
    }

    @Override
    public void commencer(Planification planning) {
        System.out.println("[State] Impossible : planning déjà terminé !");
    }

    @Override
    public void terminer(Planification planning) {
        System.out.println("[State] Planning déjà terminé !");
    }

    @Override
    public void annuler(Planification planning) {
        System.out.println("[State] Planning #" + planning.getId() + " : TERMINÉ → ANNULE");
        planning.setEtatCourant(new AnnuleState());
    }

    @Override
    public String getNomEtat() {
        return "TERMINÉ";
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
        return false;
    }

    @Override
    public boolean peutAnnuler() {
        return true;
    }
}