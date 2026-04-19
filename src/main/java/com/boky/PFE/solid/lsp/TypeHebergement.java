package com.boky.PFE.solid.lsp;

public interface TypeHebergement {
    
    double calculerPrix(double prixBase, int nbNuits);
    
    String getDescription();
    
    String getIcone();
    
    double getTaxe();
}