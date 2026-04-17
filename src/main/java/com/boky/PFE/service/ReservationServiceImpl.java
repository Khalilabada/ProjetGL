package com.boky.PFE.service;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.factory.FactoryProvider;
import com.boky.PFE.factory.ServiceFactory;
import com.boky.PFE.factory.reservation.IReservation;
import com.boky.PFE.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service

public class ReservationServiceImpl extends BaseService implements ReservationService {


    @Autowired
    private FactoryProvider factoryProvider;

    @Autowired
    AnnonceService annonceService;
    @Autowired
    UtilisateurService utilisateurService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    EmailService emailService;
@Override
    public Reservation AjouterReservation(ReservationRQ model) {
    IReservation iReservation = createReservation(model.getType());
        iReservation.remplirDepuisRequest(model);
        Optional<Annonce> annonce = annonceService.getAnnonceById(model.getId_annonce());
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(model.getId_client());

        if (annonce.isPresent() && utilisateur.isPresent()) {
            Reservation reservation = (Reservation) iReservation;
            
            reservation.setAnnonce(annonce.get());
            reservation.setUtilisateur(utilisateur.get());

            Utilisateur annonceur = annonceService.UtilisateurByAnnonceur(annonce.get().getId());

            sendEmail(annonceur.getEmail(), iReservation, annonce.get().getTitre());

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
        Utilisateur client = this.ClientByReservation(reservation.getId());
        Annonce annonce = this.AnnonceByReservation(reservation.getId());
        reservation.setUtilisateur(client);
        reservation.setAnnonce(annonce);
        Optional<Reservation> reservationOptional = this.getReservationById(reservation.getId());
        if (!reservationOptional.isPresent()) {
            throw new NoSuchElementException("Reservation non trouvée avec l'id: " + reservation.getId());
        }
        reservation.setEtat(true);
        String etat = reservation.isConfirmation() ? "acceptée" : "non confirmée";
        emailService.SendSimpleMessage(
                client.getEmail(),
                "Réponse concernant votre réservation de maison - " + annonce.getTitre(),
                "Bonjour,\n\n" +
                        "Nous vous informons que votre réservation pour la maison \"" + annonce.getTitre() + "\" a été " + etat + ".\n\n" +
                        "Merci de consulter votre profil pour plus de détails.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe de gestion des réservations"
        );
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
            List<Reservation> reservationsAnnonce = reservationRepository.findByAnnonceId(annonce.getId());
            reservations.addAll(reservationsAnnonce);
        }
        return reservations;
    }

    @Override
    public void SupprimerReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
