package com.boky.PFE.factory;

import com.boky.PFE.entite.Annonceur;
import com.boky.PFE.entite.Client;
import com.boky.PFE.entite.FemmeMenage;
import com.boky.PFE.entite.SousAdmin;
import com.boky.PFE.entite.SuperAdmin;
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

    /**
     * Vérifie que le "type" fourni dans une requête correspond bien à la classe du compte.
     * Si {@code type} est null/blank, on considère qu'il n'y a pas de vérification à faire.
     */
    public static boolean typeCorrespondAuCompte(Utilisateur utilisateur, String type) {
        if (utilisateur == null) {
            return false;
        }
        if (type == null || type.isBlank()) {
            return true;
        }

        String normalized = type.trim().toLowerCase();
        String expectedSimpleName = switch (normalized) {
            case "client" -> Client.class.getSimpleName();
            case "annonceur" -> Annonceur.class.getSimpleName();
            case "femmemenage", "femme-menage", "femmedemenage" -> FemmeMenage.class.getSimpleName();
            case "sousadmin", "sous_admin" -> SousAdmin.class.getSimpleName();
            case "superadmin", "super_admin" -> SuperAdmin.class.getSimpleName();
            default -> null;
        };

        if (expectedSimpleName == null) {
            return false;
        }
        return utilisateur.getClass().getSimpleName().equalsIgnoreCase(expectedSimpleName);
    }
}
