package com.boky.PFE.service;

import com.boky.PFE.Beans.SaveEvaluation;
import com.boky.PFE.Beans.SaveEvaluationFDM;
import com.boky.PFE.factory.FactoryProvider;
import com.boky.PFE.factory.ServiceFactory;
import com.boky.PFE.factory.evaluation.IEvaluation;
import com.boky.PFE.factory.offre.Offre;
import com.boky.PFE.factory.reservation.IReservation;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService {

    @Autowired
    protected FactoryProvider factoryProvider;

    @Autowired
    protected EmailService emailService;

    protected IReservation createReservation(String type) {
        ServiceFactory factory = factoryProvider.getFactory(type);
        return factory.creerReservation();
    }

    protected void sendEmail(String to, IReservation iReservation, String titre) {
        emailService.SendSimpleMessage(
                to,
                iReservation.getSujetEmail(),
                iReservation.getCorpsEmail(titre)
        );
    }

    protected Offre createOffre(String type) {
        ServiceFactory factory = factoryProvider.getFactory(type);
        return factory.creerOffre();
    }

    protected IEvaluation createEvaluation(String type) {
        ServiceFactory factory = factoryProvider.getFactory(type);
        return factory.creerEvaluation();
    }
}