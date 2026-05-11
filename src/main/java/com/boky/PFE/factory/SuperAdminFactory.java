package com.boky.PFE.factory;

import com.boky.PFE.entite.SuperAdmin;
import com.boky.PFE.entite.Utilisateur;

public class SuperAdminFactory extends UtilisateurFactory {
    @Override
    public Utilisateur creerUtilisateur() {
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setEtat(true);
        return superAdmin;
    }
}
