package com.boky.PFE.service;

import com.boky.PFE.entite.Utilisateur;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UtilisateurService {

    ResponseEntity<Object> AjouterUtilisateur(Utilisateur utilisateur);
    Utilisateur ModifierUtilisateur(Utilisateur utilisateur, long id);
    List<Utilisateur> AfficherUtilisateur();
    void SupprimerUtilisateur (Long id);
    Optional<Utilisateur> getUtilisateurById(Long id);

    List<Utilisateur> getUtilisateurByRole(String role);
    ResponseEntity<?> ConfirmationEmail (String confirmationEmail);

}
