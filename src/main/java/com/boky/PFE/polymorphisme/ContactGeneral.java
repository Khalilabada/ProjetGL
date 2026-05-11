package com.boky.PFE.polymorphisme;

public class ContactGeneral implements TypeContact {
    
    @Override
    public String getCategorie() {
        return "Information générale";
    }
    
    @Override
    public String getPriorite() {
        return "NORMALE";
    }
    
    @Override
    public String getIcone() {
        return "📋";
    }
    
    @Override
    public String getEmailDestination() {
        return "support@boky.com";
    }
    
    @Override
    public void afficherInfo() {
        System.out.println(getIcone() + " Contact général - Priorité " + getPriorite());
    }
}