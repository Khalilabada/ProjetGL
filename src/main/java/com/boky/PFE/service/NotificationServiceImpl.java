package com.boky.PFE.service;

import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Contact;
import com.boky.PFE.entite.Planification;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.ReservationFM;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;

    public NotificationServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notifyReservationCreated(Utilisateur annonceur, Annonce annonce, Reservation reservation) {
        if (annonceur == null || annonce == null || reservation == null) {
            return;
        }
        emailService.SendSimpleMessage(
                annonceur.getEmail(),
                "Nouvelle réservation pour votre annonce",
                "Bonjour,\n\n"
                        + "Nous vous informons que votre annonce \""
                        + annonce.getTitre()
                        + "\" a été réservée. "
                        + "Veuillez consulter votre profil pour confirmer la réservation.\n\n"
                        + "Cordialement,\n"
                        + "L'équipe de gestion des réservations"
        );
    }

    @Override
    public void notifyReservationResponse(Utilisateur client, Annonce annonce, Reservation reservation) {
        if (client == null || annonce == null || reservation == null) {
            return;
        }
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
    }

    @Override
    public void notifyAnnonceStateChanged(Utilisateur annonceur, Annonce annonce, boolean nouvelEtat) {
        if (annonceur == null || annonce == null) {
            return;
        }
        String etat = nouvelEtat ? "mise en ligne" : "hors ligne";
        emailService.SendSimpleMessage(
                annonceur.getEmail(),
                "L'etat de votre Annonce " + annonce.getTitre(),
                "Votre annonce a été " + etat
        );
    }

    @Override
    public void notifyAnnonceCommentAdded(Utilisateur annonceur, Annonce annonce) {
        if (annonceur == null || annonce == null) {
            return;
        }
        emailService.SendSimpleMessage(
                annonceur.getEmail(),
                "Nouveau commentaire sur votre annonce",
                "Bonjour,\n\n"
                        + "Nous vous informons qu'un nouveau commentaire a été laissé sur votre annonce \""
                        + annonce.getTitre()
                        + "\". Veuillez consulter votre profil pour lire et répondre au commentaire.\n\n"
                        + "Cordialement,\n"
                        + "L'équipe de gestion des annonces"
        );
    }

    @Override
    public void notifyAccountStateChanged(Utilisateur utilisateur, boolean nouvelEtat) {
        if (utilisateur == null) {
            return;
        }
        String etat = nouvelEtat ? "Accepté" : "Bloqué";
        emailService.SendSimpleMessage(
                utilisateur.getEmail(),
                "L'etat de votre compte",
                "Votre compte a été " + etat
        );
    }

    @Override
    public void notifyContactResponse(Contact contact) {
        if (contact == null) {
            return;
        }
        emailService.SendSimpleMessage(
                contact.getEmail(),
                "Réponse concernant le sujet :" + contact.getSujet(),
                contact.getRepondre()
        );
    }

    @Override
    public void notifyReservationFMCreated(Planification planification) {
        if (planification == null || planification.getFdm() == null) return;

        emailService.SendSimpleMessage(
                planification.getFdm().getEmail(),
                "Nouvelle réservation pour votre planning",
                "Bonjour,\n\n"
                        + "Nous vous informons que votre planning du "
                        + planification.getJour()
                        + "a été réservée. "
                        + "Veuillez consulter votre profil pour confirmer la réservation.\n\n"
                        + "Cordialement,\n"
                        + "L'équipe de gestion des réservations"
        );
    }

    @Override
    public void notifyReservationFMResponse(Utilisateur client, Planification planification, ReservationFM reservationFM) {
        if (client == null || planification == null || reservationFM == null) {
            return;
        }
        String etat = reservationFM.isConfirmation() ? "acceptée" : "non confirmée";
        emailService.SendSimpleMessage(
                client.getEmail(),
                "Réponse concernant votre réservation de ménage",
                "Bonjour,\n\n"
                        + "Nous souhaitons vous informer que votre réservation pour le service de ménage a été "
                        + etat + " pour la date du " + planification.getJour() + ".\n\n"
                        + "Merci de consulter votre profil pour plus de détails.\n\n"
                        + "Cordialement,\n"
                        + "L'équipe de gestion des réservations"
        );
    }

    @Override
    public void notifyEvaluationFDMAdded(Utilisateur fdm) {
        if (fdm == null) {
            return;
        }
        emailService.SendSimpleMessage(
                fdm.getEmail(),
                "Nouvelle évaluation pour votre service",
                "Bonjour,\n\n"
                        + "Nous vous informons qu'une nouvelle évaluation a été laissée pour votre service. "
                        + "Veuillez consulter votre profil pour lire l'évaluation.\n\n"
                        + "Cordialement,\n"
                        + "L'équipe de gestion des services"
        );
    }
}

