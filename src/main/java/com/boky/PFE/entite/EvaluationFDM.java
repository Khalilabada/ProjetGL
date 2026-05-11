package com.boky.PFE.entite;

import com.boky.PFE.Beans.SaveEvaluationFDM;
import com.boky.PFE.factory.evaluation.IEvaluation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationFDM implements IEvaluation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;

    private long Star;
    @ManyToOne
    Client utilisateur;
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        this.date = now.format(formatter);

    }
    @ManyToOne
    FemmeMenage fdm;

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

    public Client getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Client utilisateur) {
        this.utilisateur = utilisateur;
    }

    public FemmeMenage getFdm() {
        return fdm;
    }

    public void setFdm(FemmeMenage fdm) {
        this.fdm = fdm;
    }

    @Override
    public String getType() {
        return "NETTOYAGE";
    }


    @Override
    public void remplirDepuisFDM(SaveEvaluationFDM model) {
        this.setId(model.getId());
        this.setStar(model.getStar());
        this.setDate(model.getDate());
    }
}
