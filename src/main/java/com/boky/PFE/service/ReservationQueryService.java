package com.boky.PFE.service;

import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Client;
import com.boky.PFE.entite.Reservation;

import java.util.List;
import java.util.Optional;

/**
 * Query side (reads) — ISP: only read/query operations (+ related lookups).
 */
public interface ReservationQueryService {

    Optional<Reservation> getReservationById(Long id);

    List<Reservation> AfficherReservation();

    List<Reservation> listeReservationByUtilisateur(Long id);

    List<Reservation> listReservationByAnnonceur(Long idAnnonceur);

    Client ClientByReservation(Long id);

    Annonce AnnonceByReservation(Long id);
}
