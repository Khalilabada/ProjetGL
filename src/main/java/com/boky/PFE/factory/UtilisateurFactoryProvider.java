package com.boky.PFE.factory;

import com.boky.PFE.entite.Utilisateur;

public final class UtilisateurFactoryProvider {
    private UtilisateurFactoryProvider() {
    }

    public static UtilisateurFactory getFactory(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Le type utilisateur est obligatoire.");
        }

        return switch (type.toLowerCase()) {
            case "client" -> new ClientFactory();
            case "annonceur" -> new AnnonceurFactory();
            case "femmemenage", "femme-menage", "femmedemenage" -> new FemmeMenageFactory();
            case "sousadmin","sous_admin" -> new SousAdminFactory();
            case "superadmin","super_admin" -> new SuperAdminFactory();
            default -> throw new IllegalArgumentException("Type utilisateur invalide.");
        };
    }
}
