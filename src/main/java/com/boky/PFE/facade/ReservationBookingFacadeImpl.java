package com.boky.PFE.facade;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.service.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class ReservationBookingFacadeImpl implements ReservationBookingFacade {

    /**  4 Sous-systèmes */

    private final ReservationService reservationService;

    private final EmailService emailService;


    private final AnnonceService annonceService;

    private final UtilisateurService utilisateurService;

    // Injection par constructeur
    public ReservationBookingFacadeImpl(
            ReservationService reservationService,
            EmailService emailService,
            AnnonceService annonceService,
            UtilisateurService utilisateurService) {
        this.reservationService  = reservationService;
        this.emailService        = emailService;
        this.annonceService      = annonceService;
        this.utilisateurService  = utilisateurService;
    }

    // Méthodes orchestrées
    @Override
    public Reservation creerDemandeReservation(ReservationRQ model) {

        // Étape 1 — vérification via les sous-systèmes 3 et 4
        Optional<Annonce> annonce =
                annonceService.getAnnonceById(model.getId_annonce());
        Optional<Utilisateur> client =
                utilisateurService.getUtilisateurById(model.getId_client());

        if (annonce.isEmpty() || client.isEmpty()) {
            return null;
        }

        // Étape 2 — persistance via le sous-système 1
        Reservation reservation = reservationService.AjouterReservation(model);

        // Étape 3 — notification via le sous-système 2
        Utilisateur annonceur =
                annonceService.UtilisateurByAnnonceur(annonce.get().getId());
        emailService.SendSimpleMessage(
                annonceur.getEmail(),
                "Nouvelle réservation pour votre annonce",
                "Bonjour,\n\n"
                        + "Nous vous informons que votre annonce \""
                        + annonce.get().getTitre()
                        + "\" a été réservée. "
                        + "Veuillez consulter votre profil pour confirmer la réservation.\n\n"
                        + "Cordialement,\n"
                        + "L'équipe de gestion des réservations"
        );

        return reservation;
    }

    @Override
    public Reservation enregistrerReponseReservation(Long id, Reservation reservation) {

        // Étape 1 — vérification via le sous-système 1
        Optional<Reservation> existing = reservationService.getReservationById(id);
        if (existing.isEmpty()) {
            throw new NoSuchElementException(
                    "Reservation non trouvée avec l'id: " + id);
        }

        // Récupérer client et annonce depuis la réservation existante [ss 1]
        Utilisateur client = reservationService.ClientByReservation(id);
        Annonce annonce    = reservationService.AnnonceByReservation(id);

        // Étape 2 — persistance via le sous-système 1
        Reservation reservationModifiee =
                reservationService.ModifierReservation(reservation);

        // Étape 3 — notification via le sous-système 2
        String etat = reservation.isConfirmation() ? "acceptée" : "non confirmée";
        emailService.SendSimpleMessage(
                client.getEmail(),
                "Réponse concernant votre réservation de maison - " + annonce.getTitre(),
                "Bonjour,\n\n"
                        + "Nous vous informons que votre réservation pour la maison \""
                        + annonce.getTitre()
                        + "\" a été " + etat + ".\n\n"
                        + "Merci de consulter votre profil pour plus de détails.\n\n"
                        + "Cordialement,\n"
                        + "L'équipe de gestion des réservations"
        );

        return reservationModifiee;
    }

    @Override
    public List<Reservation> afficherToutesLesReservations() {
        return reservationService.AfficherReservation();
    }

    @Override
    public List<Reservation> reservationsParClient(Long idClient) {
        return reservationService.listeReservationByUtilisateur(idClient);
    }

    @Override
    public List<Reservation> reservationsParAnnonceur(Long idAnnonceur) {
        return reservationService.listReservationByAnnonceur(idAnnonceur);
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationService.getReservationById(id);
    }

    @Override
    public Utilisateur getClientByReservation(Long id) {
        return reservationService.ClientByReservation(id);
    }

    @Override
    public Annonce getAnnonceByReservation(Long id) {
        return reservationService.AnnonceByReservation(id);
    }

    @Override
    public void supprimerReservation(Long id) {
        reservationService.SupprimerReservation(id);
    }
}