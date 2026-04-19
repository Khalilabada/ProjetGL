package com.boky.PFE.service;

import com.boky.PFE.entite.ConfirmationToken;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurEmailService {
    
    @Autowired
    private EmailUtilisateurService emailUtilisateurService;
    
    public void envoyerEmailConfirmation(Utilisateur utilisateur, ConfirmationToken token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(utilisateur.getEmail());
        mailMessage.setSubject("Confirmation de votre inscription");
        
        String message = "Bonjour " + utilisateur.getPrenom() + " " + utilisateur.getNom() + ",\n\n" +
                "Merci de vous être inscrit sur notre site. Pour compléter votre inscription, veuillez confirmer votre compte en cliquant sur le lien ci-dessous :\n\n" +
                "http://localhost:8081/api/Utilisateur/confirm-account?token=" + token.getConfirmationToken() + "\n\n" +
                "Cordialement,\n" +
                "L'équipe de support";
        
        mailMessage.setText(message);
        emailUtilisateurService.sendEmail(mailMessage);
    }
    
    public void envoyerEmailChangementEtat(Utilisateur utilisateur, String etat) {
        String message = "Bonjour,\n\nVotre compte a été " + etat + ".\n\nCordialement";
    }
}