package com.boky.PFE.Beans;

import com.boky.PFE.entite.Reservation;

import java.util.Date;

public class ReservationRQ
{
    private Long id ;
    private String date_arrivee;
    private String date_depart;
    private long nb_nuit;
    private long nb_vacancier;
    private long montant_paye;
    private String date;
    private boolean etat;
    private boolean confirmation ;

    private long id_client;
    private long id_annonce;


    public static Reservation toEntity(ReservationRQ model) {
        if (model == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(model.getId());
        reservation.setMontant_paye(model.getMontant_paye());
        reservation.setDate_depart(model.getDate_depart());
        reservation.setDate_arrivee(model.getDate_arrivee());
        reservation.setDate(model.getDate());
        reservation.setNb_nuit(model.getNb_nuit());
        reservation.setNb_vacancier(model.getNb_vacancier());
        reservation.setEtat(model.isEtat());
        reservation.setConfirmation(model.isConfirmation());

        return reservation;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public long getMontant_paye() {
        return montant_paye;
    }

    public void setMontant_paye(long montant_paye) {
        this.montant_paye = montant_paye;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getNb_vacancier() {
        return nb_vacancier;
    }

    public void setNb_vacancier(long nb_vacancier) {
        this.nb_vacancier = nb_vacancier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate_arrivee() {
        return date_arrivee;
    }

    public void setDate_arrivee(String date_arrivee) {
        this.date_arrivee = date_arrivee;
    }

    public String getDate_depart() {
        return date_depart;
    }

    public void setDate_depart(String date_depart) {
        this.date_depart = date_depart;
    }

    public long getNb_nuit() {
        return nb_nuit;
    }

    public void setNb_nuit(long nb_nuit) {
        this.nb_nuit = nb_nuit;
    }

    public long getId_client() {
        return id_client;
    }

    public void setId_client(long id_client) {
        this.id_client = id_client;
    }

    public long getId_annonce() {
        return id_annonce;
    }

    public void setId_annonce(long id_annonce) {
        this.id_annonce = id_annonce;
    }
}
