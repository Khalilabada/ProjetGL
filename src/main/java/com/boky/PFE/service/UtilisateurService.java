package com.boky.PFE.service;

import com.boky.PFE.Beans.UtilisateurRequest;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UtilisateurService {

    ResponseEntity<Object> AjouterUtilisateur(Utilisateur utilisateur);
    Utilisateur ModifierUtilisateur(Utilisateur utilisateur, long id);

    /** Mise à jour à partir du JSON {@link UtilisateurRequest} (optionnel : valider {@code type} vs la classe du compte). */
    Utilisateur modifierDepuisRequete(Long id, UtilisateurRequest request);
    List<Utilisateur> AfficherUtilisateur();
    void SupprimerUtilisateur (Long id);
    Optional<Utilisateur> getUtilisateurById(Long id);

    List<Utilisateur> getUtilisateurByRole(String role);
    ResponseEntity<?> ConfirmationEmail (String confirmationEmail);

    
    Utilisateur findByEmail(String email);

}
