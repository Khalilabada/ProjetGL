package com.boky.PFE.Beans;


import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.EvaluationFDM;

public class SaveEvaluationFDM
{
    private Long id;
    private String date;

    private long Star;

    private long id_utilisateur;

    private long id_FDM;
    public static EvaluationFDM toEntity(SaveEvaluationFDM model) {
        if (model == null) {
            return null;
        }
        EvaluationFDM evaluation = new EvaluationFDM();
        evaluation.setId(model.getId());
        evaluation.setStar(model.getStar());
        evaluation.setDate(model.getDate());

        return evaluation;
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

    public long getStar() {
        return Star;
    }

    public void setStar(long star) {
        Star = star;
    }

    public long getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(long id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public long getId_FDM() {
        return id_FDM;
    }

    public void setId_FDM(long id_FDM) {
        this.id_FDM = id_FDM;
    }
}
