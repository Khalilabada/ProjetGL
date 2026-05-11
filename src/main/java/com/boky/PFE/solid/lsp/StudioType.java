package com.boky.PFE.solid.lsp;

public class StudioType implements TypeHebergement {
    
    @Override
    public double calculerPrix(double prixBase, int nbNuits) {
        return prixBase * nbNuits * 0.9;
    }
    
    @Override
    public String getDescription() {
        return "🏠 Studio - Économique et pratique";
    }
    
    @Override
    public String getIcone() {
        return "🏠";
    }
    
    @Override
    public double getTaxe() {
        return 0.05;
    }
}