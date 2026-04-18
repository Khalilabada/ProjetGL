package com.boky.PFE.restController;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Reservation")
public class ReservationRestController
{
    @Autowired
    ReservationService reservationService;
    @RequestMapping(method = RequestMethod.POST)
    public Reservation ajouterReservation(@RequestBody ReservationRQ model){
        System.out.println("reserverRq"+model);
        return reservationService.AjouterReservation(model);
    }
    @RequestMapping(method = RequestMethod.GET)
    public List<Reservation> AfficherReservation()
    {
        return reservationService.AfficherReservation();
    }
    @RequestMapping("get-all-by-id-utilisateur/{id}")
    public List<Reservation> listReservationByUtilisateur(@PathVariable Long id){
        return reservationService.listeReservationByUtilisateur(id);
    }
    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    public Optional<Reservation> getReservationById(@PathVariable("id") long id){

        Optional<Reservation> reservation = reservationService.getReservationById(id);
        return reservation;
    }
    @RequestMapping("get-client/{id}")
    public Utilisateur ClientByReservation(@PathVariable  Long id) {
        return reservationService.ClientByReservation(id);
    }
    @RequestMapping("get-annonce/{id}")
    public Annonce AnnonceByReservation(@PathVariable  Long id) {
        return reservationService.AnnonceByReservation(id);
    }
    @RequestMapping(value = "/{id}" ,method = RequestMethod.PUT)
    public Reservation ModifierReservation(@PathVariable("id")Long id, @RequestBody Reservation reservation){
        Reservation newReservation = reservationService.ModifierReservation(reservation);
        return newReservation;
    }
    @RequestMapping("get-all-by-id-annonceur/{id}")
    public List<Reservation> listReservationByAnnonceur(@PathVariable Long id) {
    return reservationService.listReservationByAnnonceur(id);
}
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE )

    public void SupprimerReservation(@PathVariable("id") Long id){

        reservationService.SupprimerReservation(id);

    }
}
