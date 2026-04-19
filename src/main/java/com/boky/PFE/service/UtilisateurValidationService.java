package com.boky.PFE.service;

import com.boky.PFE.entite.Utilisateur;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurValidationService {
    
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    
    public boolean emailEstValide(String email) {
        return email != null && email.contains("@");
    }
    
    public boolean motDePasseEstValide(String mdp) {
        return mdp != null && mdp.length() >= 6;
    }
    
    public String encoderMotDePasse(String mdp) {
        return this.bCryptPasswordEncoder.encode(mdp);
    }
    
    public boolean motDePasseCorrespond(String mdpRaw, String mdpEncode) {
        return this.bCryptPasswordEncoder.matches(mdpRaw, mdpEncode);
    }
    
    public boolean utilisateurEstActif(Utilisateur utilisateur) {
        return utilisateur != null && utilisateur.isEtat();
    }
}