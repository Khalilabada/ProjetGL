package com.boky.PFE.service;

import com.boky.PFE.Beans.SaveAnnonce;
import com.boky.PFE.entite.Admin;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface AnnonceService
{
    Annonce AjouterAnnonce(SaveAnnonce model);
    Annonce ModifierAnnonce(Annonce annonce);
    List<Annonce> AfficherAnnonce();
    void SupprimerAnnonce (Long id);
    Optional<Annonce> getAnnonceById(Long id);
     List<Annonce> listeAnnonceByAnnonceur(Long id);

    Utilisateur UtilisateurByAnnonceur(Long id);
}

