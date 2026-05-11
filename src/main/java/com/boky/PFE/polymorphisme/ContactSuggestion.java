package com.boky.PFE.polymorphisme;

public class ContactSuggestion implements TypeContact {
    
    @Override
    public String getCategorie() {
        return "Suggestion";
    }
    
    @Override
    public String getPriorite() {
        return "BASSE";
    }
    
    @Override
    public String getIcone() {
        return "💡";
    }
    
    @Override
    public String getEmailDestination() {
        return "product@boky.com";
    }
    
    @Override
    public void afficherInfo() {
        System.out.println(getIcone() + " Suggestion - Priorité " + getPriorite());
    }
}