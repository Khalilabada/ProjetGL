package com.boky.PFE.service;

import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReservationNotificationService {

    @Autowired
    private EmailService emailService;

    public void notifierNouvelleReservation(Reservation reservation) {
        Utilisateur annonceur = reservation.getAnnonce().getAnnonceur();

        emailService.SendSimpleMessage(
                annonceur.getEmail(),
                "Nouvelle réservation pour votre annonce",
                "Bonjour,\n\n" +
                        "Nous vous informons que votre annonce \"" + reservation.getAnnonce().getTitre() + "\" a été réservée. " +
                        "Veuillez consulter votre profil pour confirmer la réservation.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe de gestion des réservations"
        );
    }


    public void notifierChangementReservation(Reservation reservation) {
        Utilisateur client = reservation.getUtilisateur();

        emailService.SendSimpleMessage(
                client.getEmail(),
                reservation.getSujetNotification(),
                reservation.getCorpsNotification()
        );
    }
}
