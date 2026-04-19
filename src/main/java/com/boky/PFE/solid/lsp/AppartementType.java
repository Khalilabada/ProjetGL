package com.boky.PFE.solid.lsp;

public class AppartementType implements TypeHebergement {
    
    @Override
    public double calculerPrix(double prixBase, int nbNuits) {
        return prixBase * nbNuits;
    }
    
    @Override
    public String getDescription() {
        return "🏢 Appartement - Idéal pour un séjour en ville";
    }
    
    @Override
    public String getIcone() {
        return "🏢";
    }
    
    @Override
    public double getTaxe() {
        return 0.10;
    }
}