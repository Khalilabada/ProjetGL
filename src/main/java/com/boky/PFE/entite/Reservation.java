package com.boky.PFE.entite;

import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.Beans.SavereservationFM;
import com.boky.PFE.factory.reservation.IReservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reservation implements IReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date_arrivee;
    private String date_depart;
    private long nb_nuit;
    private long nb_vacancier;
    private long montant_paye;
    private String date;
    private boolean etat;
    private boolean confirmation;

    @ManyToOne
    private Utilisateur utilisateur;

    public String getDate_arrivee() {
        return date_arrivee;
    }

    public String getDate_depart() {
        return date_depart;
    }

    public long getNb_nuit() {
        return nb_nuit;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate_arrivee(String date_arrivee) {
        this.date_arrivee = date_arrivee;
    }

    public void setDate_depart(String date_depart) {
        this.date_depart = date_depart;
    }

    public void setNb_nuit(long nb_nuit) {
        this.nb_nuit = nb_nuit;
    }

    public void setNb_vacancier(long nb_vacancier) {
        this.nb_vacancier = nb_vacancier;
    }

    public void setMontant_paye(long montant_paye) {
        this.montant_paye = montant_paye;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public long getNb_vacancier() {
        return nb_vacancier;
    }

    public long getMontant_paye() {
        return montant_paye;
    }

    public String getDate() {
        return date;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public Annonce getAnnonce() {
        return annonce;
    }

    @ManyToOne
    private Annonce annonce;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.date = now.format(formatter);

    }

    // --- IMPLÉMENTATION DE IReservation (ABSTRACT FACTORY) ---

    @Override
    public void remplirDepuisRequest(ReservationRQ model) {
        this.id = model.getId();
        this.date_arrivee = model.getDate_arrivee();
        this.date_depart = model.getDate_depart();
        this.nb_nuit = model.getNb_nuit();
        this.nb_vacancier = model.getNb_vacancier();
        this.montant_paye = model.getMontant_paye();
        this.etat = model.isEtat();
        this.confirmation = model.isConfirmation();
    }




    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Cette méthode fait le pont entre l'attribut montant_paye
     * et la méthode attendue par l'interface IReservation.
     */
    @Override
    public long getMontantPaye() {
        return this.montant_paye;
    }

    @Override
    public boolean isConfirmation() {
        return false;
    }

    @Override
    public boolean isEtat() {
        return false;
    }

    @Override
    public String getSujetEmail() {
        return "Nouvelle réservation d'hébergement";
    }

    @Override
    public String getCorpsEmail(String titreAnnonce) {
        return "Bonjour,\n\nNous vous informons que votre hébergement \"" + titreAnnonce +
                "\" a été réservé.\nVeuillez consulter votre profil pour confirmer.\n\nCordialement.";
    }

    @Override
    public String getType() {
        return "HEBERGEMENT";
    }

    // --- CONSTRUCTEURS ---

}