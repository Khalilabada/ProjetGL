package com.boky.PFE.service;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.exceptions.AccesRefuseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.boky.PFE.util.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@Service
@Primary
public class ReservationServiceProxy implements ReservationService {

    @Autowired
    @Qualifier("reservationServiceImpl")
    private ReservationService realService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public Reservation AjouterReservation(ReservationRQ model) {
        return realService.AjouterReservation(model);
    }

    @Override
    public List<Reservation> AfficherReservation() {
        return realService.AfficherReservation();
    }

    @Override
    public List<Reservation> listeReservationByUtilisateur(Long id) {
        return realService.listeReservationByUtilisateur(id);
    }

    @Override
    public Utilisateur ClientByReservation(Long id) {
        return realService.ClientByReservation(id);
    }

    @Override
    public Annonce AnnonceByReservation(Long id) {
        return realService.AnnonceByReservation(id);
    }

    @Override
    public Reservation ModifierReservation(Reservation reservation) {
        // Vérification : Seul l'Annonceur (propriétaire) peut confirmer la réservation
        Reservation existing = realService.getReservationById(reservation.getId())
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        // Simulation ID utilisateur courant
        Long currentUserId = getCurrentUserId();

        if (currentUserId == null || !existing.getAnnonce().getAnnonceur().getId().equals(currentUserId)) {
            throw new AccesRefuseException("Accès refusé : vous devez être connecté et être le propriétaire de l'annonce pour modifier cette réservation.");
        }

        return realService.ModifierReservation(reservation);
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return realService.getReservationById(id);
    }

    @Override
    public List<Reservation> listReservationByAnnonceur(Long id) {
        return realService.listReservationByAnnonceur(id);
    }

    @Override
    public void SupprimerReservation(Long id) {
        Reservation existing = realService.getReservationById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        Long currentUserId = getCurrentUserId();

        // Le client ou l'annonceur peuvent supprimer/annuler, mais doivent être connectés
        boolean isClient = currentUserId != null && existing.getUtilisateur().getId().equals(currentUserId);
        boolean isAnnonceur = currentUserId != null && existing.getAnnonce().getAnnonceur().getId().equals(currentUserId);

        if (currentUserId == null || (!isClient && !isAnnonceur)) {
            throw new AccesRefuseException("Accès refusé : vous n'avez pas l'autorisation de supprimer cette réservation (non connecté ou non autorisé).");
        }

        realService.SupprimerReservation(id);
    }

    private Long getCurrentUserId() {
        return AuthUtils.getCurrentUserId(request);
    }
}
