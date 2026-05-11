package com.boky.PFE.service;

import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurPersistenceService {
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    public Utilisateur sauvegarder(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }
    
    public Optional<Utilisateur> trouverParId(Long id) {
        return utilisateurRepository.findById(id);
    }
    
    public Utilisateur trouverParEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
    
    public List<Utilisateur> trouverTous() {
        return utilisateurRepository.findAll();
    }
    
    public List<Utilisateur> trouverParRole(String role) {
        return utilisateurRepository.findUtilisateursByRole(role);
    }
    
    public void supprimer(Long id) {
        utilisateurRepository.deleteById(id);
    }
    
    public boolean emailExiste(String email) {
        return utilisateurRepository.findByEmail(email) != null;
    }
}