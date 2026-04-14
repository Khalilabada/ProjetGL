package com.boky.PFE.entite;

import com.boky.PFE.factory.offre.Offre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Planification implements Offre
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

    @Override
    public String getTitre() {
        return "Service de nettoyage - " + this.gouvernorat;
    }

  
    @Override
    public float getPrix() {
        try {
            return Float.parseFloat(this.prixParHeure);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

  
    @Override
    public String getType() {
        return "NETTOYAGE";
    }
}
