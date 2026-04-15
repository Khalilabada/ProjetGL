package com.boky.PFE.entite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Entity

public class Reservation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String date_arrivee;
    private String date_depart;
    private long nb_nuit;
    private long nb_vacancier;
    private long montant_paye;
    private String date;
    private boolean etat;
    private boolean confirmation ;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd ");
        this.date = now.format(formatter);
        this.etat=false;
        this.confirmation=false;

    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
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

    @ManyToOne
    Utilisateur utilisateur;
    @ManyToOne
    Annonce annonce ;

    public long getNb_vacancier() {
        return nb_vacancier;
    }

    public void setNb_vacancier(long nb_vacancier) {
        this.nb_vacancier = nb_vacancier;
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



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Annonce getAnnonce() {
        return annonce;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public Reservation(Long id, String date_arrivee, String date_depart, long nb_nuit, long nb_vacancier, long montant_paye, String date, Utilisateur utilisateur, Annonce annonce) {
        this.id = id;
        this.date_arrivee = date_arrivee;
        this.date_depart = date_depart;
        this.nb_nuit = nb_nuit;
        this.nb_vacancier = nb_vacancier;
        this.montant_paye = montant_paye;
        this.date = date;
        this.utilisateur = utilisateur;
        this.annonce = annonce;
    }
    public Reservation( String date_arrivee, String date_depart, long nb_nuit, long nb_vacancier, long montant_paye, Utilisateur utilisateur, Annonce annonce) {

        this.date_arrivee = date_arrivee;
        this.date_depart = date_depart;
        this.nb_nuit = nb_nuit;
        this.nb_vacancier = nb_vacancier;
        this.montant_paye = montant_paye;

        this.utilisateur = utilisateur;
        this.annonce = annonce;
    }

    public Reservation( String date_arrivee, String date_depart, long nb_nuit, long nb_vacancier, long montant_paye,  boolean etat, boolean confirmation, Utilisateur utilisateur, Annonce annonce) {

        this.date_arrivee = date_arrivee;
        this.date_depart = date_depart;
        this.nb_nuit = nb_nuit;
        this.nb_vacancier = nb_vacancier;
        this.montant_paye = montant_paye;

        this.etat = etat;
        this.confirmation = confirmation;
        this.utilisateur = utilisateur;
        this.annonce = annonce;
    }

    public Reservation() {

    }
}
