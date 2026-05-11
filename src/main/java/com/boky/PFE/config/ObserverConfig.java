package com.boky.PFE.config;

import pattern.observer.AdminObserver;
import pattern.observer.LogObserver;
import com.boky.PFE.service.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class ObserverConfig {

    @Autowired
    private ReservationServiceImpl reservationService;

    @EventListener(ApplicationReadyEvent.class)
    public void registerObservers() {
        System.out.println("=========================================");
        System.out.println("ENREGISTREMENT DES OBSERVATEURS");
        
        reservationService.attach(new AdminObserver());
        reservationService.attach(new LogObserver());
        
        System.out.println("2 observateurs enregistrés !");
        System.out.println("=========================================");
    }
}