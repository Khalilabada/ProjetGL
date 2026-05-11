package com.boky.PFE.service;

import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Contact;
import com.boky.PFE.entite.Planification;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.ReservationFM;
import com.boky.PFE.entite.Utilisateur;

public interface NotificationService {

    // Réservations
    void notifyReservationCreated(Utilisateur annonceur, Annonce annonce, Reservation reservation);

    void notifyReservationResponse(Utilisateur client, Annonce annonce, Reservation reservation);

    // Annonces
    void notifyAnnonceStateChanged(Utilisateur annonceur, Annonce annonce, boolean nouvelEtat);

    void notifyAnnonceCommentAdded(Utilisateur annonceur, Annonce annonce);

    // Compte utilisateur
    void notifyAccountStateChanged(Utilisateur utilisateur, boolean nouvelEtat);

    // Contact
    void notifyContactResponse(Contact contact);

    // Reservation FM
    void notifyReservationFMCreated(Planification planification);

    void notifyReservationFMResponse(Utilisateur client, Planification planification, ReservationFM reservationFM);

    // Evaluation FDM
    void notifyEvaluationFDMAdded(Utilisateur fdm);
}

