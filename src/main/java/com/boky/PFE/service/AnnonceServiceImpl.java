package com.boky.PFE.service;

import com.boky.PFE.Beans.SaveAnnonce;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.factory.ServiceFactory;
import com.boky.PFE.factory.offre.Offre;
import com.boky.PFE.repository.AnnonceRepository;
import com.boky.PFE.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AnnonceServiceImpl implements AnnonceService {

    private final ServiceFactory factory;

    @Autowired
    public AnnonceServiceImpl(@Qualifier("hebergementFactory") ServiceFactory factory) {
        this.factory = factory;
    }

    @Autowired
    AnnonceRepository annonceRepository;
    @Autowired
    UtilisateurRepository utilisateurRepository;
    @Autowired
    EmailService emailService;

    @Override
    public Annonce AjouterAnnonce(SaveAnnonce model) {
        Offre offre = factory.creerOffre();

        Annonce annonce = (Annonce) offre;
        annonce.setTitre(model.getTitre());
        annonce.setDescription(model.getDescription());
        annonce.setType_d_hebergement(model.getType_d_hebergement());
        annonce.setNb_voyageur(model.getNb_voyageur());
        annonce.setNb_chamber(model.getNb_chamber());
        annonce.setNb_lits(model.getNb_lits());
        annonce.setNb_salles(model.getNb_salles());
        annonce.setEquipement(model.getEquipement());
        annonce.setEquipement_specail(model.getEquipement_specail());
        annonce.setEquipement_securite(model.getEquipement_securite());
        annonce.setImage(model.getImage());
        annonce.setReduction_semaine(model.isReduction_semaine());
        annonce.setReduction_mois(model.isReduction_mois());
        annonce.setPrix(model.getPrix());
        annonce.setPays(model.getPays());
        annonce.setVille(model.getVille());
        annonce.setCode_postale(model.getCode_postale());
        annonce.setHeure_depart(model.getHeure_depart());
        annonce.setHeure_arriver(model.getHeure_arriver());

        System.out.println("[Factory] Offre créée via HebergementFactory — type: " + offre.getType());
        System.out.println("idAnnonceur: " + model.getId_annonceur());

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
