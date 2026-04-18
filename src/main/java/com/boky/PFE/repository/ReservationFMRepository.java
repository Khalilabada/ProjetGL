package com.boky.PFE.repository;

import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.ReservationFM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationFMRepository extends JpaRepository<ReservationFM,Long> {


    List<ReservationFM> findByUtilisateurId(Long id);

    List<ReservationFM> findByPlanificationId(Long id);
}
