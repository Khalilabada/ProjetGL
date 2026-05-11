package com.boky.PFE.factory;

import com.boky.PFE.entite.Annonceur;
import com.boky.PFE.entite.Utilisateur;

public class AnnonceurFactory extends UtilisateurFactory {
    @Override
    public Utilisateur creerUtilisateur() {
        Annonceur annonceur = new Annonceur();
        annonceur.setEtat(true);
        return annonceur;
    }
}

