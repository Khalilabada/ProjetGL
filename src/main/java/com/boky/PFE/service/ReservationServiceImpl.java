package com.boky.PFE.service;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import pattern.observer.AdminObserver;
import pattern.observer.LogObserver;
import com.boky.PFE.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    
    @Autowired
    AnnonceService annonceService;
    
    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    ReservationRepository reservationRepository;
    
    @Autowired
    EmailService emailService;
    
    // ========== PATTERN OBSERVER ==========
    private List<Object> observers = new ArrayList<>();
    
    public void attach(Object observer) {
        observers.add(observer);
        System.out.println("[Observer] Observateur ajouté: " + observer.getClass().getSimpleName());
    }
    
    public void detach(Object observer) {
        observers.remove(observer);
        System.out.println("[Observer] Observateur retiré: " + observer.getClass().getSimpleName());
    }
    
    public void notifyObservers() {
        System.out.println("[Observer] Notification de " + observers.size() + " observateur(s)");
        for (Object observer : observers) {
            if (observer instanceof AdminObserver) {
                ((AdminObserver) observer).update();
            }
            if (observer instanceof LogObserver) {
                ((LogObserver) observer).update();
            }
        }
    }
    
    // ========== METHODES METIER ==========
    
    @Override
    public Reservation AjouterReservation(ReservationRQ model) {
        System.out.println("[ReservationService] Création d'une nouvelle réservation");
        
        Reservation reservation = ReservationRQ.toEntity(model);
        Optional<Annonce> annonce = annonceService.getAnnonceById(model.getId_annonce());
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(model.getId_client());

        if (annonce.isPresent() && utilisateur.isPresent()) {
            Utilisateur annonceur = annonceService.UtilisateurByAnnonceur(annonce.get().getId());
            
            reservation.setAnnonce(annonce.get());
            reservation.setUtilisateur(utilisateur.get());
            
            // Envoi d'email existant (ACTION PRINCIPALE)
            emailService.SendSimpleMessage(
                    annonceur.getEmail(),
                    "Nouvelle réservation pour votre annonce",
                    "Bonjour,\n\n" +
                            "Nous vous informons que votre annonce \"" + annonce.get().getTitre() + "\" a été réservée. " +
                            "Veuillez consulter votre profil pour confirmer la réservation.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe de gestion des réservations"
            );

            Reservation savedReservation = reservationRepository.save(reservation);
            
            // Notification aux observateurs (ACTIONS SECONDAIRES)
            notifyObservers();
            
            System.out.println("[ReservationService] Réservation créée avec succès. ID: " + savedReservation.getId());
            return savedReservation;
        } else {
            System.err.println("[ReservationService] Erreur: Annonce ou utilisateur non trouvé");
            return null;
        }
    }

    @Override
    public List<Reservation> AfficherReservation() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> listeReservationByUtilisateur(Long id) {
        return reservationRepository.findByutilisateurId(id);
    }

    @Override
    public Utilisateur ClientByReservation(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            return reservation.get().getUtilisateur();
        }
        return null;
    }
    
    @Override
    public Annonce AnnonceByReservation(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            return reservation.get().getAnnonce();
        }
        return null;
    }

    @Override
    public Reservation ModifierReservation(Reservation reservation) {
        System.out.println("[ReservationService] Modification de la réservation ID: " + reservation.getId());
        
        Utilisateur client = this.ClientByReservation(reservation.getId());
        Annonce annonce = this.AnnonceByReservation(reservation.getId());
        reservation.setUtilisateur(client);
        reservation.setAnnonce(annonce);
        
        Optional<Reservation> reservationOptional = this.getReservationById(reservation.getId());
        if (!reservationOptional.isPresent()) {
            throw new NoSuchElementException("Reservation non trouvée avec l'id: " + reservation.getId());
        }
        
        reservation.setEtat(true);

        String etat = reservation.isConfirmation() ? "acceptée" : "non confirmée";
        emailService.SendSimpleMessage(
                client.getEmail(),
                "Réponse concernant votre réservation de maison - " + annonce.getTitre(),
                "Bonjour,\n\n" +
                        "Nous vous informons que votre réservation pour la maison \"" + annonce.getTitre() + "\" a été " + etat + ".\n\n" +
                        "Merci de consulter votre profil pour plus de détails.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe de gestion des réservations"
        );

        Reservation updatedReservation = reservationRepository.save(reservation);
        
        System.out.println("[ReservationService] Réservation modifiée avec succès");
        return updatedReservation;
    }
    
    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }
    
    @Override
    public List<Reservation> listReservationByAnnonceur(Long idAnnonceur) {
        List<Annonce> annonces = annonceService.listeAnnonceByAnnonceur(idAnnonceur);
        List<Reservation> reservations = new ArrayList<>();

        for (Annonce annonce : annonces) {
            List<Reservation> reservationsAnnonce = reservationRepository.findByAnnonceId(annonce.getId());
            reservations.addAll(reservationsAnnonce);
        }
        return reservations;
    }
    
    @Override
    public void SupprimerReservation(Long id) {
        System.out.println("[ReservationService] Suppression de la réservation ID: " + id);
        reservationRepository.deleteById(id);
        System.out.println("[ReservationService] Réservation supprimée avec succès");
    }
}