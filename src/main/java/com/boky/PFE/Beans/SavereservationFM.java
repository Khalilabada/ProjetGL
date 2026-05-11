package com.boky.PFE.Beans;

import com.boky.PFE.entite.ReservationFM;

/**
 * Form Model class for saving reservation details.
 */
public class SavereservationFM {
    private Long id;
    private String date;
    private long montant_paye;
    private boolean etat;
    private boolean confirmation;
    private long id_client;
    private long id_planification;


    

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getMontant_paye() {
        return montant_paye;
    }

    public void setMontant_paye(long montant_paye) {
        this.montant_paye = montant_paye;
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

    public long getId_client() {
        return id_client;
    }

    public void setId_client(long id_client) {
        this.id_client = id_client;
    }

    public long getId_planification() {
        return id_planification;
    }

    public void setId_planification(long id_planification) {
        this.id_planification = id_planification;
    }


    public String getType() {
        return "NETTOYAGE";
    }

}
