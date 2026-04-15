package com.boky.PFE.entite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Planification
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String heureDisponible;
    private String jour;
    private String gouvernorat;
    private String adresse;
    private String prixParHeure;
    @ManyToOne
    Utilisateur fdm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getFdm() {
        return fdm;
    }

    public void setFdm(Utilisateur fdm) {
        this.fdm = fdm;
    }

    public String getPrixParHeure() {
        return prixParHeure;
    }

    public void setPrixParHeure(String prixParHeure) {
        this.prixParHeure = prixParHeure;
    }

    public String getHeureDisponible() {
        return heureDisponible;
    }

    public void setHeureDisponible(String heureDisponible) {
        this.heureDisponible = heureDisponible;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getGouvernorat() {
        return gouvernorat;
    }

    public void setGouvernorat(String gouvernorat) {
        this.gouvernorat = gouvernorat;
    }
}
