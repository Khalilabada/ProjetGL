package com.boky.PFE.factory;

import com.boky.PFE.entite.SousAdmin;
import com.boky.PFE.entite.Utilisateur;

public class SousAdminFactory extends UtilisateurFactory {
    @Override
    public Utilisateur creerUtilisateur() {
        SousAdmin sousAdmin = new SousAdmin();
        sousAdmin.setEtat(false);
        return sousAdmin;
    }
}
