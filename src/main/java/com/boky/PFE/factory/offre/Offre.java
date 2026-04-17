package com.boky.PFE.factory.offre;


import com.boky.PFE.Beans.SaveAnnonce;
import com.boky.PFE.Beans.SavePlanification;

public interface Offre {
    Long getId();
    String getTitre();
    float getPrix();
    String getType();
    default void remplirDepuisRequest(SaveAnnonce model) {}
    default void remplirDepuisPlanification(SavePlanification model) {}
}
