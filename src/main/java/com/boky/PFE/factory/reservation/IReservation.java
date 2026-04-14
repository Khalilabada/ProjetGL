package com.boky.PFE.factory.reservation;


public interface IReservation {
    Long getId();
    long getMontantPaye();
    boolean isConfirmation();
    boolean isEtat();
    String getType(); 
}
