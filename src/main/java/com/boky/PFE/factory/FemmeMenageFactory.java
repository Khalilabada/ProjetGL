package com.boky.PFE.factory;

import com.boky.PFE.entite.FemmeMenage;
import com.boky.PFE.entite.Utilisateur;

public class FemmeMenageFactory extends UtilisateurFactory {
    @Override
    public Utilisateur creerUtilisateur() {
        FemmeMenage femmeMenage = new FemmeMenage();
        femmeMenage.setEtat(true);
        return femmeMenage;
    }
}
