package com.boky.PFE.service;
import com.boky.PFE.Beans.SaveAnnonce;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.repository.AnnonceRepository;
import com.boky.PFE.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AnnonceServiceImpl implements AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationService notificationService;

    public AnnonceServiceImpl(
            AnnonceRepository annonceRepository,
            UtilisateurRepository utilisateurRepository,
            NotificationService notificationService) {
        this.annonceRepository = annonceRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Annonce AjouterAnnonce(SaveAnnonce model) {
        Annonce annonce = SaveAnnonce.toEntity(model);
        System.out.println("idAnnonceur: " + model.getId_annonceur());

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(model.getId_annonceur());
        if (utilisateurOptional.isPresent()) {
            Utilisateur utilisateur = utilisateurOptional.get();
            annonce.setAnnonceur(utilisateur);
            return annonceRepository.save(annonce);
        } else {
            // Gérer le cas où l'utilisateur n'est pas trouvé
            return null; // Ou lancer une exception, selon le cas
        }
    }

    @Override
    public Annonce ModifierAnnonce(Annonce annonce) {
        System.out.println("hatha annonce.getAnnonceur() "+annonce.getAnnonceur());
        Utilisateur annonceur = this.UtilisateurByAnnonceur(annonce.getId());
        System.out.println("hatha annonceur "+annonceur);
        annonce.setAnnonceur(annonceur);
        System.out.println("hatha annonce "+annonce);
        Optional<Annonce> annonceOptional = this.getAnnonceById(annonce.getId());
        if (!annonceOptional.isPresent()) {
            throw new NoSuchElementException("Annonce non trouvée avec l'id: " + annonce.getId());
        }

        Annonce annonce1 = annonceOptional.get();
        if (annonce1.isEtat() != annonce.isEtat() && annonce1.isVerification()) {
            notificationService.notifyAnnonceStateChanged(annonceur, annonce, annonce.isEtat());
        }
        return annonceRepository.save(annonce);
    }

    @Override
    public List<Annonce> AfficherAnnonce() {
        return annonceRepository.findAll();
    }

    @Override
    public void SupprimerAnnonce(Long id) {

        annonceRepository.deleteById(id);
    }

    @Override
    public Optional<Annonce> getAnnonceById(Long id) {
        return annonceRepository.findById(id);
    }
    public List<Annonce> listeAnnonceByAnnonceur(Long id) {
        return annonceRepository.findByAnnonceurId(id);
    }
    public Utilisateur UtilisateurByAnnonceur(  Long id) {
        Optional<Annonce> annonce =  annonceRepository.findById(id);
        return annonce.get().getAnnonceur();
    }
}
