package com.boky.PFE.Beans;

import com.boky.PFE.entite.Annonce;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

import java.util.List;

public class SaveAnnonce {
    private Long id;
    private String type_d_hebergement;
    private int nb_voyageur;
    private int nb_chamber;
    private int nb_lits;
    private int nb_salles;
    private List<String> equipement; // Changez le type de données en List<String>

    @ElementCollection
    private List<String> equipement_specail;

    @ElementCollection
    private List<String> equipement_securite;
    @ElementCollection
    @Column(name = "image", columnDefinition = "LONGTEXT")
    private List<String> image;
    private String titre;
    private String description;


    private boolean reduction_semaine;
    private boolean reduction_mois;
    private float prix;
    private String pays;
    private boolean etat;
    private String ville;
    private String code_postale;
    private String heure_depart;
    private String heure_arriver;
    private String date;
    private boolean verification;
    private boolean accorde_user;
    private long id_annonceur;

    public static Annonce toEntity(SaveAnnonce model) {
        if (model == null) {
            return null;
        }
        Annonce annonce = new Annonce();
        annonce.setId(model.getId());
        annonce.setType_d_hebergement(model.getType_d_hebergement());
        annonce.setNb_voyageur(model.getNb_voyageur());
        annonce.setNb_chamber(model.getNb_chamber());
        annonce.setNb_lits(model.getNb_lits());
        annonce.setNb_salles(model.getNb_salles());
        annonce.setEquipement(model.getEquipement());
        annonce.setEquipement_specail(model.getEquipement_specail());
        annonce.setEquipement_securite(model.getEquipement_securite());
        annonce.setImage(model.getImage());
        annonce.setTitre(model.getTitre());
        annonce.setDescription(model.getDescription());
        annonce.setReduction_semaine(model.isReduction_semaine());
        annonce.setReduction_mois(model.isReduction_mois());
        annonce.setPrix(model.getPrix());
        annonce.setPays(model.getPays());
        annonce.setEtat(model.isEtat());
        annonce.setVille(model.getVille());
        annonce.setCode_postale(model.getCode_postale());
        annonce.setHeure_depart(model.getHeure_depart());
        annonce.setHeure_arriver(model.getHeure_arriver());
        annonce.setDate(model.getDate());
        annonce.setVerification(model.isVerification());
        annonce.setAccorde_user(model.isAccorde_user());

        return annonce;
    }


    public boolean isAccorde_user() {
        return accorde_user;
    }

    public void setAccorde_user(boolean accorde_user) {
        this.accorde_user = accorde_user;
    }

    public boolean isVerification() {
        return verification;
    }

    public void setVerification(boolean verification) {
        this.verification = verification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType_d_hebergement() {
        return type_d_hebergement;
    }

    public void setType_d_hebergement(String type_d_hebergement) {
        this.type_d_hebergement = type_d_hebergement;
    }

    public int getNb_voyageur() {
        return nb_voyageur;
    }

    public void setNb_voyageur(int nb_voyageur) {
        this.nb_voyageur = nb_voyageur;
    }

    public int getNb_chamber() {
        return nb_chamber;
    }

    public void setNb_chamber(int nb_chamber) {
        this.nb_chamber = nb_chamber;
    }

    public int getNb_lits() {
        return nb_lits;
    }

    public void setNb_lits(int nb_lits) {
        this.nb_lits = nb_lits;
    }

    public int getNb_salles() {
        return nb_salles;
    }

    public void setNb_salles(int nb_salles) {
        this.nb_salles = nb_salles;
    }


    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




    public boolean isReduction_semaine() {
        return reduction_semaine;
    }

    public void setReduction_semaine(boolean reduction_semaine) {
        this.reduction_semaine = reduction_semaine;
    }

    public boolean isReduction_mois() {
        return reduction_mois;
    }

    public void setReduction_mois(boolean reduction_mois) {
        this.reduction_mois = reduction_mois;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }


    public String getCode_postale() {
        return code_postale;
    }

    public void setCode_postale(String code_postale) {
        this.code_postale = code_postale;
    }

    public String getHeure_depart() {
        return heure_depart;
    }

    public void setHeure_depart(String heure_depart) {
        this.heure_depart = heure_depart;
    }

    public String getHeure_arriver() {
        return heure_arriver;
    }

    public void setHeure_arriver(String heure_arriver) {
        this.heure_arriver = heure_arriver;
    }

    public long getId_annonceur() {
        return id_annonceur;
    }

    public void setId_annonceur(long id_annonceur) {
        this.id_annonceur = id_annonceur;
    }

    public List<String> getEquipement() {
        return equipement;
    }

    public void setEquipement(List<String> equipement) {
        this.equipement = equipement;
    }

    public List<String> getEquipement_specail() {
        return equipement_specail;
    }

    public void setEquipement_specail(List<String> equipement_specail) {
        this.equipement_specail = equipement_specail;
    }

    public List<String> getEquipement_securite() {
        return equipement_securite;
    }

    public void setEquipement_securite(List<String> equipement_securite) {
        this.equipement_securite = equipement_securite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}


