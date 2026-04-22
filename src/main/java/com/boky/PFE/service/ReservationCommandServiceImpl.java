package com.boky.PFE.service;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Annonceur;
import com.boky.PFE.entite.Client;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final AnnonceService annonceService;
    private final UtilisateurService utilisateurService;
    private final EmailService emailService;

    public ReservationCommandServiceImpl(
            ReservationRepository reservationRepository,
            AnnonceService annonceService,
            UtilisateurService utilisateurService,
            EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.annonceService = annonceService;
        this.utilisateurService = utilisateurService;
        this.emailService = emailService;
    }

    @Override
    public Reservation AjouterReservation(ReservationRQ model) {
        Reservation reservation = ReservationRQ.toEntity(model);

        Annonce annonceEntity = annonceService.getAnnonceById(model.getId_annonce())
                .orElseThrow(() -> new NoSuchElementException("Annonce non trouvée avec l'id: " + model.getId_annonce()));

        Utilisateur utilisateurReserveur = utilisateurService.getUtilisateurById(model.getId_client())
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé avec l'id: " + model.getId_client()));
        if (!(utilisateurReserveur instanceof Client client)) {
            throw new IllegalArgumentException("L'utilisateur reserveur doit etre un Client.");
        }

        Annonceur annonceur = annonceService.UtilisateurByAnnonceur(annonceEntity.getId());

        reservation.setAnnonce(annonceEntity);
        reservation.setUtilisateur(client);

        emailService.SendSimpleMessage(
                annonceur.getEmail(),
                "Nouvelle réservation pour votre annonce",
                "Bonjour,\n\n"
                        + "Nous vous informons que votre annonce \"" + annonceEntity.getTitre() + "\" a été réservée. "
                        + "Veuillez consulter votre profil pour confirmer la réservation.\n\n"
                        + "Cordialement,\n"
                        + "L'équipe de gestion des réservations"
        );

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation ModifierReservation(Reservation reservation) {
        Reservation existing = reservationRepository.findById(reservation.getId())
                .orElseThrow(() -> new NoSuchElementException("Reservation non trouvée avec l'id: " + reservation.getId()));

        Client client = existing.getUtilisateur();
        Annonce annonce = existing.getAnnonce();

        reservation.setUtilisateur(client);
        reservation.setAnnonce(annonce);
        reservation.setEtat(true);

        String etat = reservation.isConfirmation() ? "acceptée" : "non confirmée";

        emailService.SendSimpleMessage(
                client.getEmail(),
                "Réponse concernant votre réservation de maison - " + annonce.getTitre(),
                "Bonjour,\n\n"
                        + "Nous vous informons que votre réservation pour la maison \"" + annonce.getTitre() + "\" a été " + etat + ".\n\n"
                        + "Merci de consulter votre profil pour plus de détails.\n\n"
                        + "Cordialement,\n"
                        + "L'équipe de gestion des réservations"
        );

        return reservationRepository.save(reservation);
    }

    @Override
    public void SupprimerReservation(Long id) {
        if (!reservationRepository.existsById(id))
    throw new NoSuchElementException("Reservation introuvable : " + id);
reservationRepository.deleteById(id);
    }
}
