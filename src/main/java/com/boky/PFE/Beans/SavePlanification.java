package com.boky.PFE.Beans;


import com.boky.PFE.entite.Planification;


public class SavePlanification
{

    private Long id;
    private String heureDisponible;
    private String jour;
    private String adresse;
    private String prixParHeure;
    private String gouvernorat;
    private long id_fdm;
    public static Planification toEntity(SavePlanification model) {
        if (model == null) {
            return null;
        }
        Planification planification = new Planification();
        planification.setId(model.getId());
        planification.setHeureDisponible(model.getHeureDisponible());
        planification.setJour(model.getJour());
        planification.setAdresse(model.getAdresse());
        planification.setPrixParHeure(model.getPrixParHeure());
        planification.setGouvernorat(model.getGouvernorat());

        return planification;
    }

    public String getGouvernorat() {
        return gouvernorat;
    }

    public void setGouvernorat(String gouvernorat) {
        this.gouvernorat = gouvernorat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPrixParHeure() {
        return prixParHeure;
    }

    public void setPrixParHeure(String prixParHeure) {
        this.prixParHeure = prixParHeure;
    }

    public long getId_fdm() {
        return id_fdm;
    }

    public void setId_fdm(long id_fdm) {
        this.id_fdm = id_fdm;
    }
}
