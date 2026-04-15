package com.boky.PFE.entite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationFDM
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;

    private long Star;
    @ManyToOne
    Utilisateur utilisateur;
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        this.date = now.format(formatter);

    }
    @ManyToOne
    Utilisateur fdm;

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

    public long getStar() {
        return Star;
    }

    public void setStar(long star) {
        Star = star;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Utilisateur getFdm() {
        return fdm;
    }

    public void setFdm(Utilisateur fdm) {
        this.fdm = fdm;
    }
}
