package com.boky.PFE.service;

import com.boky.PFE.Beans.SaveAnnonce;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.exceptions.AccesRefuseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.boky.PFE.util.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


 //PROXY DE SÉCURITÉ pour AnnonceService.
 // Ce patron de structure contrôle l'accès au service réel.

@Service
@Primary
public class AnnonceServiceProxy implements AnnonceService {

    @Autowired
    @Qualifier("annonceServiceImpl") // Injecte spécifiquement le service réel
    private AnnonceService realService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private HttpServletRequest request;


    @Override
    public void SupprimerAnnonce(Long id) {

        Annonce annonce = realService.getAnnonceById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));
        Long currentUserId = getCurrentUserId();

        // Logique de contrôle du Proxy : Bloquer si non connecté OU si n'est pas le propriétaire
        Long ownerId = annonce.getAnnonceur().getId();
        System.out.println("DEBUG PROXY - User Connecté: " + currentUserId);
        System.out.println("DEBUG PROXY - Propriétaire Annonce: " + ownerId);

        if (currentUserId == null || !ownerId.equals(currentUserId)) {
            throw new AccesRefuseException("Accès refusé : vous devez être connecté et être le propriétaire pour supprimer cette annonce !");
        }
        realService.SupprimerAnnonce(id);
    }

    @Override
    public Annonce ModifierAnnonce(Annonce annonce) {
        // Vérifier si l'annonce existe et appartient à l'utilisateur
        Annonce existing = realService.getAnnonceById(annonce.getId())
                .orElseThrow(() -> new RuntimeException("Annonce inaccessible"));

        Long currentUserId = getCurrentUserId();

        if (currentUserId == null || !existing.getAnnonceur().getId().equals(currentUserId)) {
            throw new AccesRefuseException("Action interdite : vous devez être le propriétaire pour modifier cette annonce.");
        }

        return realService.ModifierAnnonce(annonce);
    }



    @Override
    public Annonce AjouterAnnonce(SaveAnnonce model) {
        return realService.AjouterAnnonce(model);
    }

    @Override
    public List<Annonce> AfficherAnnonce() {
        return realService.AfficherAnnonce();
    }

    @Override
    public Optional<Annonce> getAnnonceById(Long id) {
        return realService.getAnnonceById(id);
    }

    @Override
    public List<Annonce> listeAnnonceByAnnonceur(Long id) {
        return realService.listeAnnonceByAnnonceur(id);
    }

    @Override
    public Utilisateur UtilisateurByAnnonceur(Long id) {
        return realService.UtilisateurByAnnonceur(id);
    }

    private Long getCurrentUserId() {
        return AuthUtils.getCurrentUserId(request);
    }

}
