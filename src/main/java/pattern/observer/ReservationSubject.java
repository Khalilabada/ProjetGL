package pattern.observer;


import com.boky.PFE.entite.Reservation;

public interface ReservationSubject {
    void attach(ReservationObserver observer);
    void detach(ReservationObserver observer);
    void notifyObservers(Reservation reservation, String evenementType);
}