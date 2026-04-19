package com.boky.PFE.polymorphisme;

public class ContactTechnique implements TypeContact {
    
    @Override
    public String getCategorie() {
        return "Problème technique";
    }
    
    @Override
    public String getPriorite() {
        return "ÉLEVÉE";
    }
    
    @Override
    public String getIcone() {
        return "🔧";
    }
    
    @Override
    public String getEmailDestination() {
        return "tech@boky.com";
    }
    
    @Override
    public void afficherInfo() {
        System.out.println(getIcone() + " Contact technique - Priorité " + getPriorite());
    }
}