package com.boky.PFE.service;

import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Client;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationRepository reservationRepository;
    private final AnnonceService annonceService;

    public ReservationQueryServiceImpl(
            ReservationRepository reservationRepository,
            AnnonceService annonceService) {
        this.reservationRepository = reservationRepository;
        this.annonceService = annonceService;
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> AfficherReservation() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> listeReservationByUtilisateur(Long id) {
        return reservationRepository.findByutilisateurId(id);
    }

    @Override
    public List<Reservation> listReservationByAnnonceur(Long idAnnonceur) {
        List<Annonce> annonces = annonceService.listeAnnonceByAnnonceur(idAnnonceur);
        List<Reservation> reservations = new ArrayList<>();
        for (Annonce annonce : annonces) {
            reservations.addAll(reservationRepository.findByAnnonceId(annonce.getId()));
        }
        return reservations;
    }

    @Override
    public Client ClientByReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation non trouvée avec l'id: " + id))
                .getUtilisateur();
    }

    @Override
    public Annonce AnnonceByReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation non trouvée avec l'id: " + id))
                .getAnnonce();
    }
}
