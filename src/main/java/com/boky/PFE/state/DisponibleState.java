package com.boky.PFE.state;


import com.boky.PFE.entite.Planification;

public class DisponibleState implements PlanningState {

    @Override
    public void reserver(Planification planning) {
        System.out.println("[State] Planning #" + planning.getId() + " : DISPONIBLE → RÉSERVÉ");
        planning.setEtatCourant(new ReserveState());
    }

    @Override
    public void commencer(Planification planning) {
        System.out.println("[State] Impossible de commencer : planning pas encore réservé !");
    }

    @Override
    public void terminer(Planification planning) {
        System.out.println("[State] Impossible de terminer : planning pas encore commencé !");
    }

    @Override
    public void annuler(Planification planning) {
        System.out.println("[State] Planning #" + planning.getId() + " : DISPONIBLE → ANNULE");
        planning.setEtatCourant(new AnnuleState());
    }

    @Override
    public String getNomEtat() {
        return "DISPONIBLE";
    }

    @Override
    public boolean peutReserver() {
        return true;
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