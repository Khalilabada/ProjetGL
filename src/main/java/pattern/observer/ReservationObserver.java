package pattern.observer;


import com.boky.PFE.entite.Reservation;

public interface ReservationObserver {
    void update(Reservation reservation, String evenementType);
}