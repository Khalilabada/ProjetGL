package com.boky.PFE.state;


import com.boky.PFE.entite.Planification;

public class AnnuleState implements PlanningState {

    @Override
    public void reserver(Planification planning) {
        System.out.println("[State] Impossible : planning annulé !");
    }

    @Override
    public void commencer(Planification planning) {
        System.out.println("[State] Impossible : planning annulé !");
    }

    @Override
    public void terminer(Planification planning) {
        System.out.println("[State] Impossible : planning annulé !");
    }

    @Override
    public void annuler(Planification planning) {
        System.out.println("[State] Planning déjà annulé !");
    }

    @Override
    public String getNomEtat() {
        return "ANNULE";
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
        return false;
    }
}