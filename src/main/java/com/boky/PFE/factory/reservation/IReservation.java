package com.boky.PFE.factory.reservation;


import com.boky.PFE.Beans.ReservationRQ;
import com.boky.PFE.Beans.SavereservationFM;

public interface IReservation {
    Long getId();
    long getMontantPaye();
    boolean isConfirmation();
    boolean isEtat();
    String getType(); 
    String getSujetEmail();
    String getCorpsEmail(String titreAnnonce);
    default void remplirDepuisRequest(ReservationRQ model) {}
    default void remplirDepuisFM(SavereservationFM model) {}
    }
