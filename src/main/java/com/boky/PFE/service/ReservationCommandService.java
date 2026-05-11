package com.boky.PFE.service;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Reservation;

/**
 * Command side (writes) — ISP: only mutation operations.
 */
public interface ReservationCommandService {

    Reservation AjouterReservation(ReservationRQ model);

    Reservation ModifierReservation(Reservation reservation);

    void SupprimerReservation(Long id);
}
