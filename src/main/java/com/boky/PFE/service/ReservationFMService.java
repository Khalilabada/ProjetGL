package com.boky.PFE.service;

import com.boky.PFE.Beans.SavereservationFM;
import com.boky.PFE.entite.Planification;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.ReservationFM;
import com.boky.PFE.entite.Utilisateur;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing ReservationFM entities.
 */
public interface ReservationFMService {


    ReservationFM AjouterReservationFM(SavereservationFM model);


    List<ReservationFM> AfficherReservationFM();


    List<ReservationFM> listeReservationFMByUtilisateur(Long id);


    List<ReservationFM> listeReservationFMByPlanning(Long id);

    Utilisateur ClientByReservationFM(Long id);

        Planification planificationByReservationFM(Long id);


    ReservationFM ModifierReservationFM(ReservationFM reservationFM);


    Optional<ReservationFM> getReservationFMById(Long id);

    List<ReservationFM> listReservationByFDM(Long id);

    void SupprimerReservationFDM(Long id);
}
