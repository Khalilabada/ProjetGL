package com.boky.PFE.entite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        this.date = now.format(formatter);
        this.repondre="Non répondu";
}
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRepondre() {
        return repondre;
    }

    public void setRepondre(String repondre) {
        this.repondre = repondre;
    }

    public String getSujet() {
        return sujet;
    }

    public String getMsg() {
        return msg;
    }

    public String getTelephone() {
        return telephone;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

