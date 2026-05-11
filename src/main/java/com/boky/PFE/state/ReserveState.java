package com.boky.PFE.state;


import com.boky.PFE.entite.Planification;

public class ReserveState implements PlanningState {

    @Override
    public void reserver(Planification planning) {
        System.out.println("[State] Planning déjà réservé ! Double réservation impossible.");
    }

    @Override
    public void commencer(Planification planning) {
        System.out.println("[State] Planning #" + planning.getId() + " : RÉSERVÉ → OCCUPÉ");
        planning.setEtatCourant(new OccupeState());
    }

    @Override
    public void terminer(Planification planning) {
        System.out.println("[State] Impossible de terminer : travail pas encore commencé !");
    }

    @Override
    public void annuler(Planification planning) {
        System.out.println("[State] Planning #" + planning.getId() + " : RÉSERVÉ → ANNULE");
        planning.setEtatCourant(new AnnuleState());
    }

    @Override
    public String getNomEtat() {
        return "RÉSERVÉ";
    }

    @Override
    public boolean peutReserver() {
        return false;
    }

    @Override
    public boolean peutCommencer() {
        return true;
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