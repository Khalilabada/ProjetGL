package com.boky.PFE.service;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private AnnonceService annonceService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Reservation AjouterReservation(ReservationRQ model) {
        Reservation reservation = ReservationRQ.toEntity(model);

        Optional<Annonce> annonce =
                annonceService.getAnnonceById(model.getId_annonce());
        Optional<Utilisateur> utilisateur =
                utilisateurService.getUtilisateurById(model.getId_client());

        if (annonce.isPresent() && utilisateur.isPresent()) {
            reservation.setAnnonce(annonce.get());
            reservation.setUtilisateur(utilisateur.get());
            return reservationRepository.save(reservation);
        }
        return null;
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
    public Utilisateur ClientByReservation(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.get().getUtilisateur();
    }

    @Override
    public Annonce AnnonceByReservation(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.get().getAnnonce();
    }

    @Override
    public Reservation ModifierReservation(Reservation reservation) {
        Utilisateur client  = this.ClientByReservation(reservation.getId());
        Annonce     annonce = this.AnnonceByReservation(reservation.getId());

        reservation.setUtilisateur(client);
        reservation.setAnnonce(annonce);

        Optional<Reservation> existing = this.getReservationById(reservation.getId());
        if (!existing.isPresent()) {
            throw new NoSuchElementException(
                    "Reservation non trouvee avec l'id: " + reservation.getId());
        }
        reservation.setEtat(true);
        return reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> listReservationByAnnonceur(Long idAnnonceur) {
        List<Annonce> annonces = annonceService.listeAnnonceByAnnonceur(idAnnonceur);
        List<Reservation> reservations = new ArrayList<>();
        for (Annonce annonce : annonces) {
            reservations.addAll(
                    reservationRepository.findByAnnonceId(annonce.getId()));
        }
        return reservations;
    }

    @Override
    public void SupprimerReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}