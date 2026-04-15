package com.boky.PFE.repository;

import com.boky.PFE.entite.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {
    boolean existsByEmail(String email);

    Utilisateur findUtilisateurByEmail(String email);

    Utilisateur findByEmail(String email);

    List<Utilisateur> findUtilisateursByRole(String role);

    List<Utilisateur> findUtilisateurByRole(String role);
}
