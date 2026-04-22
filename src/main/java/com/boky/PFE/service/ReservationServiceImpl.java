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
    ReservationNotificationService notificationService;
    @Override
    public Reservation AjouterReservation(ReservationRQ model){
        // CONTRAT D'OPÉRATION : Précondition
        if (model == null || model.getId_annonce() <= 0 || model.getId_client()<= 0) {
            throw new IllegalArgumentException("Les données de la requête sont incomplètes (Precondition OCL)");
        }
        Reservation reservation = ReservationRQ.toEntity(model);
        Optional<Annonce> annonce = annonceService.getAnnonceById(model.getId_annonce());
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(model.getId_client());
        System.out.println("====== DEBUG ======");
        System.out.println("ID_ANN: " + model.getId_annonce() + ", Present? " + annonce.isPresent());
        System.out.println("ID_CLI: " + model.getId_client() + ", Present? " + utilisateur.isPresent());


        if (annonce.isPresent() && utilisateur.isPresent()) {
            Utilisateur annonceur = annonceService.UtilisateurByAnnonceur(annonce.get().getId());
            if (!utilisateur.get().isEtat()) {
                throw new IllegalStateException("Seul un utilisateur actif peut effectuer une réservation");
            }

            // Contrainte de realtion : Multiplicité logique (annoncea ctive)
            if (!annonce.get().isEtat()) {
                throw new IllegalStateException("L'annonce n'est plus disponible pour une réservation");
            }

            // Contrainte de relation: pasAuto-réservation
            if (utilisateur.get().getId().equals(annonceur.getId())) {
                throw new IllegalStateException("Violation OCL : Un propriétaire ne peut pas réserver sa propre annonce");
            }

            // Invariants sur les attributs
            if (reservation.getNb_vacancier() <= 0) {
                throw new IllegalArgumentException("Le nombre de vacanciers doit être strictement positif");
            }

            if (reservation.getDate_arrivee().compareTo(reservation.getDate_depart()) >= 0) {
                throw new IllegalArgumentException("La date d'arrivée doit être inferieur à la date de départ");
            }

            // Contrainte postcondition:
            float montantAttendu = annonce.get().getPrix() * reservation.getNb_nuit();
            reservation.setMontant_paye((long) montantAttendu);

            reservation.setAnnonce(annonce.get());
            reservation.setUtilisateur(utilisateur.get());

            // SRP     //  CONTRAT D'OPÉRATION : Postcondition (SRP)
            notificationService.notifierNouvelleReservation(reservation);

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

        // GRASP Expert
        reservation.traiter();

       // SRP
        notificationService.notifierChangementReservation(reservation);

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
