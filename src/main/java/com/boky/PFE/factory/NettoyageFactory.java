package com.boky.PFE.factory;

import com.boky.PFE.entite.EvaluationFDM;
import com.boky.PFE.entite.Planification;
import com.boky.PFE.entite.ReservationFM;
import com.boky.PFE.factory.evaluation.IEvaluation;
import com.boky.PFE.factory.offre.Offre;
import com.boky.PFE.factory.reservation.IReservation;
import org.springframework.stereotype.Component;


@Component("nettoyageFactory")
public class NettoyageFactory implements ServiceFactory {

    @Override
    public Offre creerOffre() {
        return new Planification();
    }

    @Override
    public IReservation creerReservation() {
        return new ReservationFM();
    }

    @Override
    public IEvaluation creerEvaluation() {
        return new EvaluationFDM();
    }
}
