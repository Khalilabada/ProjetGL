package com.boky.PFE.service;

import com.boky.PFE.entite.ConfirmationToken;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {
    
    @Autowired
    private UtilisateurPersistenceService persistenceService;
    
    @Autowired
    private UtilisateurTokenService tokenService;
    
    @Autowired
    private UtilisateurEmailService emailService;
    
    @Autowired
    private UtilisateurValidationService validationService;
    
    @Override
    public ResponseEntity<Object> AjouterUtilisateur(Utilisateur utilisateur) {
        HashMap<String, Object> response = new HashMap<>();
        
        if (persistenceService.emailExiste(utilisateur.getEmail())) {
            response.put("message", "Email is already in use!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        utilisateur.setMdp(validationService.encoderMotDePasse(utilisateur.getMdp()));
        
        Utilisateur savedUser = persistenceService.sauvegarder(utilisateur);
        
        ConfirmationToken token = tokenService.creerToken(savedUser);
        
        emailService.envoyerEmailConfirmation(savedUser, token);
        
        System.out.println("Confirmation Token: " + token.getConfirmationToken());
        
        return ResponseEntity.ok("Verify email by the link sent on your email address");
    }
    
    @Override
    public Utilisateur ModifierUtilisateur(Utilisateur utilisateur, long id) {
        return persistenceService.sauvegarder(utilisateur);
    }
    
    @Override
    public List<Utilisateur> AfficherUtilisateur() {
        System.out.println("wsol lhna");
        return persistenceService.trouverTous();
    }
    
    @Override
    public void SupprimerUtilisateur(Long idUtilisateur) {
        Optional<Utilisateur> optionalUtilisateur = persistenceService.trouverParId(idUtilisateur);
        
        if (optionalUtilisateur.isPresent()) {
            Utilisateur utilisateur = optionalUtilisateur.get();
            tokenService.supprimerTokenParUtilisateur(utilisateur);
            persistenceService.supprimer(idUtilisateur);
        }
    }
    
    @Override
    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return persistenceService.trouverParId(id);
    }
    
    @Override
    public ResponseEntity<?> ConfirmationEmail(String confirmationEmail) {
        ConfirmationToken token = tokenService.trouverParToken(confirmationEmail);
        
        if (token != null) {
            Utilisateur utilisateur = persistenceService.trouverParEmail(token.getUtilisateur().getEmail());
            System.out.println("email from token " + token.getUtilisateur().getEmail());
            utilisateur.setEtat(true);
            persistenceService.sauvegarder(utilisateur);
            return ResponseEntity.ok("Email verified successfully! http://localhost:4200/login");
        }
        
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }
    
    @Override
    public List<Utilisateur> getUtilisateurByRole(String role) {
        return persistenceService.trouverParRole(role);
    }
}