package com.boky.PFE.entite;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private Long tokenId;

    @Column(name="confirmation_token")
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = Utilisateur.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = false, name = "Utilisateur_id")
    private Utilisateur utilisateur;

    // ✅ CONSTRUCTEUR PAR DÉFAUT (OBLIGATOIRE pour JPA/Hibernate)
    public ConfirmationToken() {
    }

    // Constructeur pour créer un nouveau token
    public ConfirmationToken(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.createdDate = new Date();
        this.confirmationToken = UUID.randomUUID().toString();
    }

    // Getters
    public Long getTokenId() {
        return tokenId;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    // Setters
    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}	