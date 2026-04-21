package com.boky.PFE.service;

import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.util.NewPassword;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UtilisateurService {

    ResponseEntity<Object> AjouterUtilisateur(Utilisateur utilisateur);

    Utilisateur ModifierUtilisateur(Utilisateur utilisateur, long id);

    List<Utilisateur> AfficherUtilisateur();

    void SupprimerUtilisateur(Long id);

    Optional<Utilisateur> getUtilisateurById(Long id);

    List<Utilisateur> getUtilisateurByRole(String role);

    ResponseEntity<?> ConfirmationEmail(String confirmationEmail);

    Utilisateur findUtilisateurByEmail(String email);

    Utilisateur getUtilisateurByEmail(String email);

    List<Utilisateur> getUtilisateurListByRole(String role);

    Optional<Utilisateur> updateUtilisateur(Long id, Utilisateur payload);

    ResponseEntity<Map<String, Object>> loginUtilisateur(Utilisateur credentials);

    ResponseEntity<Map<String, Object>> requestPasswordResetCode(Utilisateur utilisateur);

    ResponseEntity<Map<String, Object>> resetPasswordWithCode(NewPassword newPassword);

    ResponseEntity<Map<String, Object>> sendAnnonceReminderEmail(Utilisateur utilisateur);
}
