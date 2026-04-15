package com.boky.PFE.entite;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class ReservationFM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    private long montant_paye;
    private boolean etat;
    private boolean confirmation;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Planification planification;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.date = now.format(formatter);
        this.etat = false;
        this.confirmation = false;
    }

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

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }


    public Planification getPlanification() {
        return planification;
    }

    public void setPlanification(Planification planification) {
        this.planification = planification;
    }

    // Constructors
    public ReservationFM(Long id, long montant_paye, String date, Utilisateur utilisateur, Planification planification) {
        this.id = id;
        this.montant_paye = montant_paye;
        this.date = date;
        this.utilisateur = utilisateur;
        this.planification = planification;
    }

    public ReservationFM(long montant_paye, Utilisateur utilisateur, Planification planification) {
        this.montant_paye = montant_paye;
        this.utilisateur = utilisateur;
        this.planification = planification;
    }

    public ReservationFM(long montant_paye, boolean etat, boolean confirmation, Utilisateur utilisateur, Planification planification) {
        this.montant_paye = montant_paye;
        this.etat = etat;
        this.confirmation = confirmation;
        this.utilisateur = utilisateur;
        this.planification = planification;
    }

    public ReservationFM() {
    }
}
