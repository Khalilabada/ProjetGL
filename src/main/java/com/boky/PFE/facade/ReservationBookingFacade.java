package com.boky.PFE.facade;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;

import java.util.List;
import java.util.Optional;


public interface ReservationBookingFacade {

    Reservation creerDemandeReservation(ReservationRQ model);


    Reservation enregistrerReponseReservation(Long id, Reservation reservation);

    // Opérations de consultation

    List<Reservation> afficherToutesLesReservations();

    List<Reservation> reservationsParClient(Long idClient);

    List<Reservation> reservationsParAnnonceur(Long idAnnonceur);

    Optional<Reservation> getReservationById(Long id);

    Utilisateur getClientByReservation(Long id);

    Annonce getAnnonceByReservation(Long id);

    void supprimerReservation(Long id);
}