package com.boky.PFE.entite;

import com.boky.PFE.polymorphisme.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String sujet;
    private String msg;
    private String telephone;
    private String repondre;
    private String date;
    
    @Transient
    private TypeContact typeContact;
    
    public Contact() {
    }
    
    public Contact(String sujet) {
        this.sujet = sujet;
        determinerType();
    }
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        this.date = now.format(formatter);
        this.repondre = "Non répondu";
        
        determinerType();
    }
    
    @PostLoad
    public void determinerType() {
        if (sujet == null) {
            this.typeContact = new ContactGeneral();
            return;
        }
        
        String sujetLower = sujet.toLowerCase();
        
        System.out.println("=== DÉTECTION DU TYPE ===");
        System.out.println("Sujet: " + sujet);
        System.out.println("Sujet en minuscule: " + sujetLower);
        
        if (sujetLower.contains("reclamation") || 
            sujetLower.contains("plainte") || 
            sujetLower.contains("litige") ||
            sujetLower.contains("réclamation")) {
            
            System.out.println("✅ Type détecté: RÉCLAMATION");
            this.typeContact = new ContactReclamation();
        }
        else if (sujetLower.contains("bug") || 
                 sujetLower.contains("erreur") ||
                 sujetLower.contains("technique") ||
                 sujetLower.contains("connexion")) {
            
            System.out.println("✅ Type détecté: TECHNIQUE");
            this.typeContact = new ContactTechnique();
        }
        else if (sujetLower.contains("suggestion") ||
                 sujetLower.contains("idée") ||
                 sujetLower.contains("amelioration") ||
                 sujetLower.contains("amélioration")) {
            
            System.out.println("✅ Type détecté: SUGGESTION");
            this.typeContact = new ContactSuggestion();
        }
        else {
            System.out.println("✅ Type détecté: GÉNÉRAL");
            this.typeContact = new ContactGeneral();
        }
    }
    
    
    public String getCategorie() {
        if (typeContact == null) {
            determinerType();
        }
        return typeContact != null ? typeContact.getIcone() + " " + typeContact.getCategorie() : "📋 Information générale";
    }
    
    public String getPriorite() {
        if (typeContact == null) {
            determinerType();
        }
        return typeContact != null ? typeContact.getPriorite() : "NORMALE";
    }
    
    public String getEmailDestination() {
        if (typeContact == null) {
            determinerType();
        }
        return typeContact != null ? typeContact.getEmailDestination() : "support@boky.com";
    }
    
    public TypeContact getTypeContact() {
        if (typeContact == null) {
            determinerType();
        }
        return typeContact;
    }
    
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSujet() { 
        return sujet; 
    }
    
    public void setSujet(String sujet) { 
        this.sujet = sujet; 
        determinerType();  
    }
    
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getRepondre() { return repondre; }
    public void setRepondre(String repondre) { this.repondre = repondre; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}