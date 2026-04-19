package com.boky.PFE.solid.lsp;

public class VillaType implements TypeHebergement {
    
    @Override
    public double calculerPrix(double prixBase, int nbNuits) {
        return prixBase * nbNuits * 1.2;
    }
    
    @Override
    public String getDescription() {
        return "🏡 Villa - Luxe, confort et espace";
    }
    
    @Override
    public String getIcone() {
        return "🏡";
    }
    
    @Override
    public double getTaxe() {
        return 0.15;
    }
}