package com.boky.PFE.repository;

import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {


    List<Reservation> findByutilisateurId(Long id);

    List<Reservation> findByAnnonceId(Long id);
}
