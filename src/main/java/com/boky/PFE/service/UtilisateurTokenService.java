package com.boky.PFE.service;

import com.boky.PFE.entite.ConfirmationToken;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurTokenService {
    
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    
    public ConfirmationToken creerToken(Utilisateur utilisateur) {
        ConfirmationToken token = new ConfirmationToken(utilisateur);
        return confirmationTokenRepository.save(token);
    }
    
    public ConfirmationToken trouverParToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }
    
    public ConfirmationToken trouverParUtilisateur(Utilisateur utilisateur) {
        return confirmationTokenRepository.findByUtilisateur(utilisateur);
    }
    
    public void supprimerToken(Long id) {
        confirmationTokenRepository.deleteById(id);
    }
    
    public void supprimerTokenParUtilisateur(Utilisateur utilisateur) {
        ConfirmationToken token = trouverParUtilisateur(utilisateur);
        if (token != null) {
            confirmationTokenRepository.delete(token);
        }
    }
}