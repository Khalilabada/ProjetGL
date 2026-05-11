package com.boky.PFE.factory;

import com.boky.PFE.factory.evaluation.IEvaluation;
import com.boky.PFE.factory.offre.Offre;
import com.boky.PFE.factory.reservation.IReservation;


public interface ServiceFactory {


    Offre creerOffre();

    IReservation creerReservation();

    IEvaluation creerEvaluation();
}
