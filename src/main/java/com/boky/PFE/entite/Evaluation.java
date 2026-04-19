package com.boky.PFE.entite;

import com.boky.PFE.solid.ocp.EvaluationType;
import com.boky.PFE.solid.ocp.LogementEvaluation;
import com.boky.PFE.solid.ocp.ServiceEvaluation;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Evaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    private String commentaire;
    
    @ManyToOne
    Utilisateur utilisateur;
    
    @ManyToOne
    Annonce annonce;
    
    @Transient
    private EvaluationType evaluationType;
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        this.date = now.format(formatter);
        determinerTypeEvaluation();
    }
    
    @PostLoad
    public void determinerTypeEvaluation() {
        if (commentaire == null) {
            this.evaluationType = new LogementEvaluation();
            return;
        }
        
        String commentaireLower = commentaire.toLowerCase();
        
        // Détection du type d'évaluation
        if (commentaireLower.contains("service") || 
            commentaireLower.contains("accueil") ||
            commentaireLower.contains("support") ||
            commentaireLower.contains("client")) {
            this.evaluationType = new ServiceEvaluation();
        } else {
            this.evaluationType = new LogementEvaluation();
        }
    }
    
    // ========== MÉTHODES DÉLÉGUÉES À L'OCP ==========
    
    public String getCategorieEvaluation() {
        return evaluationType != null ? evaluationType.getCategorie() : "🏠 Évaluation de logement";
    }
    
    public String getMessageRecommandation() {
        return evaluationType != null ? evaluationType.getMessageRecommandation() : "Merci pour votre évaluation !";
    }
    
    public double getCoefficientPonderation() {
        return evaluationType != null ? evaluationType.getCoefficientPonderation() : 1.0;
    }
    
    public void traiterEvaluation() {
        if (evaluationType != null) {
            evaluationType.traiterEvaluation(this);
        }
    }
    
    public EvaluationType getEvaluationType() {
        return evaluationType;
    }
    
    
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
    
    public String getCommentaire() {
        return commentaire;
    }
    
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
        determinerTypeEvaluation();
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
}