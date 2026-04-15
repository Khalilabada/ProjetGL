package com.boky.PFE.restController;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.facade.ReservationBookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Reservation")
public class ReservationRestController {


    @Autowired
    private ReservationBookingFacade reservationBookingFacade;

    @RequestMapping(method = RequestMethod.POST)
    public Reservation ajouterReservation(@RequestBody ReservationRQ model) {
        return reservationBookingFacade.creerDemandeReservation(model);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Reservation> afficherReservations() {
        return reservationBookingFacade.afficherToutesLesReservations();
    }

    @RequestMapping("get-all-by-id-utilisateur/{id}")
    public List<Reservation> reservationsParClient(@PathVariable Long id) {
        return reservationBookingFacade.reservationsParClient(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Reservation> getReservationById(@PathVariable("id") Long id) {
        return reservationBookingFacade.getReservationById(id);
    }

    @RequestMapping("get-client/{id}")
    public Utilisateur getClientByReservation(@PathVariable Long id) {
        return reservationBookingFacade.getClientByReservation(id);
    }

    @RequestMapping("get-annonce/{id}")
    public Annonce getAnnonceByReservation(@PathVariable Long id) {
        return reservationBookingFacade.getAnnonceByReservation(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Reservation modifierReservation(@PathVariable("id") Long id,
                                           @RequestBody Reservation reservation) {
        return reservationBookingFacade.enregistrerReponseReservation(id, reservation);
    }

    @RequestMapping("get-all-by-id-annonceur/{id}")
    public List<Reservation> reservationsParAnnonceur(@PathVariable Long id) {
        return reservationBookingFacade.reservationsParAnnonceur(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void supprimerReservation(@PathVariable("id") Long id) {
        reservationBookingFacade.supprimerReservation(id);
    }
}