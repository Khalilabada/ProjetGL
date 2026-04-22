package com.boky.PFE.restController;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.entite.Client;
import com.boky.PFE.service.ReservationCommandService;
import com.boky.PFE.service.ReservationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Reservation")
public class ReservationRestController {

    @Autowired
    private ReservationCommandService reservationCommandService;

    @Autowired
    private ReservationQueryService reservationQueryService;

    @RequestMapping(method = RequestMethod.POST)
    public Reservation ajouterReservation(@RequestBody ReservationRQ model) {
        System.out.println("reserverRq" + model);
        return reservationCommandService.AjouterReservation(model);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Reservation> AfficherReservation() {
        return reservationQueryService.AfficherReservation();
    }

    @RequestMapping("get-all-by-id-utilisateur/{id}")
    public List<Reservation> listReservationByUtilisateur(@PathVariable Long id) {
        return reservationQueryService.listeReservationByUtilisateur(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Reservation> getReservationById(@PathVariable("id") long id) {
        return reservationQueryService.getReservationById(id);
    }

    @RequestMapping("get-client/{id}")
    public Client ClientByReservation(@PathVariable Long id) {
        return reservationQueryService.ClientByReservation(id);
    }

    @RequestMapping("get-annonce/{id}")
    public Annonce AnnonceByReservation(@PathVariable Long id) {
        return reservationQueryService.AnnonceByReservation(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Reservation ModifierReservation(@PathVariable("id") Long id, @RequestBody Reservation reservation) {
        return reservationCommandService.ModifierReservation(reservation);
    }

    @RequestMapping("get-all-by-id-annonceur/{id}")
    public List<Reservation> listReservationByAnnonceur(@PathVariable Long id) {
        return reservationQueryService.listReservationByAnnonceur(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void SupprimerReservation(@PathVariable("id") Long id) {
        reservationCommandService.SupprimerReservation(id);
    }
}
