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

@Service("reservationServiceImpl")
public class ReservationServiceImpl implements  ReservationService
{
    @Autowired
    AnnonceService annonceService;
    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    EmailService emailService;
    @Override
    public Reservation AjouterReservation(ReservationRQ model){
        Reservation reservation = ReservationRQ.toEntity(model);
        Optional<Annonce> annonce = annonceService.getAnnonceById(model.getId_annonce());
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(model.getId_client());
        Utilisateur annonceur = annonceService.UtilisateurByAnnonceur(annonce.get().getId());

        if (annonce.isPresent() && utilisateur.isPresent()) {

            reservation.setAnnonce(annonce.get());
            reservation.setUtilisateur(utilisateur.get());
            emailService.SendSimpleMessage(
                    annonceur.getEmail(),
                    "Nouvelle réservation pour votre annonce",
                    "Bonjour,\n\n" +
                            "Nous vous informons que votre annonce \"" + annonce.get().getTitre() + "\" a été réservée. " +
                            "Veuillez consulter votre profil pour confirmer la réservation.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe de gestion des réservations"
            );

            return reservationRepository.save(reservation);}
        else{
            return null;}

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
    public Utilisateur ClientByReservation( Long id) {
        Optional<Reservation> reservation =  reservationRepository.findById(id);
        return reservation.get().getUtilisateur();
    }
    @Override
    public Annonce AnnonceByReservation( Long id) {
        Optional<Reservation> reservation =  reservationRepository.findById(id);
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
        // Récupérer toutes les annonces de l'annonceur
        List<Annonce> annonces = annonceService.listeAnnonceByAnnonceur(idAnnonceur);
        List<Reservation> reservations = new ArrayList<>();

        // Pour chaque annonce, récupérer les réservations associées
        for (Annonce annonce : annonces) {
            List<Reservation> reservationsAnnonce = reservationRepository.findByAnnonceId(annonce.getId());
            reservations.addAll(reservationsAnnonce);
        }

        return reservations;
    }
    @Override
    public void SupprimerReservation(Long id){
        reservationRepository.deleteById(id);
    }

}
