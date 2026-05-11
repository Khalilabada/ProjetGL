package com.boky.PFE.service;

import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Application le principe de Responsabilité Unique (SRP).
@Service
public class AnnonceNotificationService {

    @Autowired
    private EmailService emailService;
    public void notifierChangementEtat(Annonce annonce, String texteEtat, String emailAnnonceur) {
        emailService.SendSimpleMessage(
                emailAnnonceur,
                "L'etat de votre Annonce " + annonce.getTitre(),
                "Votre annonce a été " + texteEtat
        );
    }
}
