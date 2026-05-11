package com.boky.PFE.Beans;


import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.Reservation;

public class SaveEvaluation
{
    private Long id;
    private String date;
    private String commentaire;
    private long id_client;
    private long id_annonce;


    public static Evaluation toEntity(SaveEvaluation model) {
        if (model == null) {
            return null;
        }
        Evaluation evaluation = new Evaluation();
        evaluation.setId(model.getId());
        evaluation.setCommentaire(model.getCommentaire());
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

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public long getId_client() {
        return id_client;
    }

    public void setId_client(long id_client) {
        this.id_client = id_client;
    }

    public long getId_annonce() {
        return id_annonce;
    }

    public void setId_annonce(long id_annonce) {
        this.id_annonce = id_annonce;
    }
}
