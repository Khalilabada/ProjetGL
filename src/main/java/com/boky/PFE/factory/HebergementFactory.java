package com.boky.PFE.factory;

import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.factory.evaluation.IEvaluation;
import com.boky.PFE.factory.offre.Offre;
import com.boky.PFE.factory.reservation.IReservation;
import org.springframework.stereotype.Component;


@Component("hebergementFactory")
public class HebergementFactory implements ServiceFactory {

    @Override
    public Offre creerOffre() {
        return new Annonce();
    }

    @Override
    public IReservation creerReservation() {
        return new Reservation();
    }

    @Override
    public IEvaluation creerEvaluation() {
        return new Evaluation();
    }
}
