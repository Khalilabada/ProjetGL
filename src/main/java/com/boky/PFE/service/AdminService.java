package com.boky.PFE.service;

import com.boky.PFE.entite.Admin;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminService {

    Admin AjouterAdmin(Admin admin);

    Admin ModifierAdmin(Admin admin);

    List<Admin> AfficherAdmin();

    void SupprimerAdmin(Long id);

    Optional<Admin> getAdminById(Long id);

    boolean existsByEmail(String email);

    Admin findAdminByEmail(String email);

    ResponseEntity<?> creerAdminSiEmailDisponible(Admin admin);

    ResponseEntity<Admin> mettreAJourAdmin(Long id, Admin admin);

    ResponseEntity<Map<String, Object>> loginAdmin(Admin credentials);
}
