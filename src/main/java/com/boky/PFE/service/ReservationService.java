package com.boky.PFE.service;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface ReservationService
{
    Reservation AjouterReservation(ReservationRQ model);
    List<Reservation> AfficherReservation();
    List<Reservation> listeReservationByUtilisateur(Long id );


    Utilisateur ClientByReservation( Long id);

    Annonce AnnonceByReservation(Long id);

    Reservation ModifierReservation(Reservation reservation);

    Optional<Reservation> getReservationById(Long id);
    List<Reservation> listReservationByAnnonceur( Long id);

    void SupprimerReservation(Long id);
}
