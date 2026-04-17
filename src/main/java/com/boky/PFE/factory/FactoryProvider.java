package com.boky.PFE.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FactoryProvider {

    @Autowired
    private ServiceFactory hebergementFactory;

    @Autowired
    private ServiceFactory nettoyageFactory;

    public ServiceFactory getFactory(String type) {
        if ("HEBERGEMENT".equals(type)) {
            return hebergementFactory;
        } else if ("NETTOYAGE".equals(type)) {
            return nettoyageFactory;
        }
        throw new IllegalArgumentException("Type inconnu");
    }
}