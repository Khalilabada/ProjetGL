package com.boky.PFE.polymorphisme;

public class ContactReclamation implements TypeContact {
    
    @Override
    public String getCategorie() {
        return "Réclamation";
    }
    
    @Override
    public String getPriorite() {
        return "URGENTE";
    }
    
    @Override
    public String getIcone() {
        return "⚠️";
    }
    
    @Override
    public String getEmailDestination() {
        return "reclamation@boky.com";
    }
    
    @Override
    public void afficherInfo() {
        System.out.println(getIcone() + " Réclamation - Priorité " + getPriorite());
    }
}