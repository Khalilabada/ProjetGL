package com.boky.PFE.factory;

import com.boky.PFE.entite.Client;
import com.boky.PFE.entite.Utilisateur;

public class ClientFactory extends UtilisateurFactory {
    @Override
    public Utilisateur creerUtilisateur() {
        Client client = new Client();
        client.setEtat(true);
        return client;
    }
}
