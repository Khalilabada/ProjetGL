package com.boky.PFE.service;

import com.boky.PFE.Beans.SaveAnnonce;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.factory.FactoryProvider;
import com.boky.PFE.factory.ServiceFactory;
import com.boky.PFE.factory.offre.Offre;
import com.boky.PFE.factory.reservation.IReservation;
import com.boky.PFE.repository.AnnonceRepository;
import com.boky.PFE.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AnnonceServiceImpl extends BaseService implements AnnonceService {


    @Autowired
    AnnonceRepository annonceRepository;
    @Autowired
    UtilisateurRepository utilisateurRepository;
    @Autowired
    EmailService emailService;

    @Override
    public Annonce AjouterAnnonce(SaveAnnonce model) {
        Offre offre = createOffre(model.getType());
        offre.remplirDepuisRequest(model);

        Annonce annonce = (Annonce) offre;

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(model.getId_annonceur());
        if (utilisateurOptional.isPresent()) {
            annonce.setAnnonceur(utilisateurOptional.get());
            return annonceRepository.save(annonce);
        } else {
            return null;
        }
    }

    @Override
    public Annonce ModifierAnnonce(Annonce annonce) {
        System.out.println("hatha annonce.getAnnonceur() " + annonce.getAnnonceur());
        Utilisateur annonceur = this.UtilisateurByAnnonceur(annonce.getId());
        System.out.println("hatha annonceur " + annonceur);
        annonce.setAnnonceur(annonceur);
        System.out.println("hatha annonce " + annonce);
        Optional<Annonce> annonceOptional = this.getAnnonceById(annonce.getId());
        if (!annonceOptional.isPresent()) {
            throw new NoSuchElementException("Annonce non trouvée avec l'id: " + annonce.getId());
        }

        Annonce annonce1 = annonceOptional.get();
        if (annonce1.isEtat() != annonce.isEtat() && annonce1.isVerification()) {
            String etat = annonce.isEtat() ? "mise en ligne" : "hors ligne";
            emailService.SendSimpleMessage(annonceur.getEmail(),
                    "L'etat de votre Annonce " + annonce.getTitre(),
                    "Votre annonce a été " + etat);
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

    public Utilisateur UtilisateurByAnnonceur(Long id) {
        Optional<Annonce> annonce = annonceRepository.findById(id);
        return annonce.get().getAnnonceur();
    }
}
