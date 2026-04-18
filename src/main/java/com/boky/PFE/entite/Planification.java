package com.boky.PFE.entite;

import com.boky.PFE.state.*;
import jakarta.persistence.*;

@Entity
public class Planification {
    
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
    
    // Champ pour persister l'état en base
    private String etat;
    
    @Transient
    private PlanningState etatCourant;
    
    // Constructeur par défaut
    public Planification() {
        this.etat = "DISPONIBLE";
        this.etatCourant = new DisponibleState();
    }
    
    // ========== RECONSTRUCTION DE L'ÉTAT APRÈS CHARGEMENT ==========
    @PostLoad
    public void restaurerEtatCourant() {
        System.out.println("[PostLoad] Restauration de l'état pour planning #" + id + " - Valeur en base: " + etat);
        
        switch (etat) {
            case "DISPONIBLE":
                this.etatCourant = new DisponibleState();
                break;
            case "RÉSERVÉ":
                this.etatCourant = new ReserveState();
                break;
            case "OCCUPÉ":
                this.etatCourant = new OccupeState();
                break;
            case "TERMINÉ":
                this.etatCourant = new TermineState();
                break;
            case "ANNULE":
                this.etatCourant = new AnnuleState();
                break;
            default:
                this.etatCourant = new DisponibleState();
                this.etat = "DISPONIBLE";
        }
        
        System.out.println("[PostLoad] État restauré: " + getNomEtat());
    }
    
    // ========== MÉTHODES POUR CHANGER L'ÉTAT ==========
    
    public void reserver() {
        if ("DISPONIBLE".equals(this.etat)) {
            this.etat = "RÉSERVÉ";
            this.etatCourant = new ReserveState();
            System.out.println("[State] Planning #" + id + " : DISPONIBLE → RÉSERVÉ");
        } else {
            System.out.println("[State] Impossible de réserver : planning en état " + this.etat);
        }
    }
    
    public void commencer() {
        if ("RÉSERVÉ".equals(this.etat)) {
            this.etat = "OCCUPÉ";
            this.etatCourant = new OccupeState();
            System.out.println("[State] Planning #" + id + " : RÉSERVÉ → OCCUPÉ");
        } else {
            System.out.println("[State] Impossible de commencer : planning en état " + this.etat);
        }
    }
    
    public void terminer() {
        if ("OCCUPÉ".equals(this.etat)) {
            this.etat = "TERMINÉ";
            this.etatCourant = new TermineState();
            System.out.println("[State] Planning #" + id + " : OCCUPÉ → TERMINÉ");
        } else {
            System.out.println("[State] Impossible de terminer : planning en état " + this.etat);
        }
    }
    
    public void annuler() {
        if (!"TERMINÉ".equals(this.etat) && !"ANNULE".equals(this.etat)) {
            this.etat = "ANNULE";
            this.etatCourant = new AnnuleState();
            System.out.println("[State] Planning #" + id + " : → ANNULE");
        } else {
            System.out.println("[State] Impossible d'annuler : planning déjà terminé ou annulé");
        }
    }
    
    // ========== MÉTHODES DE VÉRIFICATION ==========
    
    public boolean peutReserver() {
        return "DISPONIBLE".equals(this.etat);
    }
    
    public boolean peutCommencer() {
        return "RÉSERVÉ".equals(this.etat);
    }
    
    public boolean peutTerminer() {
        return "OCCUPÉ".equals(this.etat);
    }
    
    public boolean peutAnnuler() {
        return !"TERMINÉ".equals(this.etat) && !"ANNULE".equals(this.etat);
    }
    
    public String getNomEtat() {
        return this.etat;  // ← Retourne directement la valeur de la base
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Utilisateur getFdm() { return fdm; }
    public void setFdm(Utilisateur fdm) { this.fdm = fdm; }
    
    public String getPrixParHeure() { return prixParHeure; }
    public void setPrixParHeure(String prixParHeure) { this.prixParHeure = prixParHeure; }
    
    public String getHeureDisponible() { return heureDisponible; }
    public void setHeureDisponible(String heureDisponible) { this.heureDisponible = heureDisponible; }
    
    public String getJour() { return jour; }
    public void setJour(String jour) { this.jour = jour; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getGouvernorat() { return gouvernorat; }
    public void setGouvernorat(String gouvernorat) { this.gouvernorat = gouvernorat; }
    
    public String getEtat() { return etat; }
    public void setEtat(String etat) { 
        this.etat = etat;
        restaurerEtatCourant();
    }
    
    public PlanningState getEtatCourant() { return etatCourant; }
    public void setEtatCourant(PlanningState etatCourant) { this.etatCourant = etatCourant; }
}