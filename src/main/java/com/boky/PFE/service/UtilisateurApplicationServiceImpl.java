package com.boky.PFE.service;

import com.boky.PFE.Beans.UtilisateurRequest;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.factory.UtilisateurFactory;
import com.boky.PFE.factory.UtilisateurFactoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurApplicationServiceImpl implements UtilisateurApplicationService {

    @Autowired
    private UtilisateurService utilisateurService;

    @Override
    public ResponseEntity<?> register(UtilisateurRequest request) {
        UtilisateurFactory factory = UtilisateurFactoryProvider.getFactory(request.getType());
        Utilisateur utilisateur = factory.creerUtilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setDate_de_naissance(request.getDate_de_naissance());
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setAdresse(request.getAdresse());
        utilisateur.setMdp(request.getMdp());
        utilisateur.setPhoto(request.getPhoto());
        return utilisateurService.AjouterUtilisateur(utilisateur);
    }
}
