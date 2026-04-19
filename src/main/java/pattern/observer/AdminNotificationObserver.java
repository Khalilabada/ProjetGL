package pattern.observer;

//nouveau fichier: com.boky.PFE.pattern.observer.AdminNotificationObserver

import com.boky.PFE.entite.Reservation;
import org.springframework.stereotype.Component;

@Component
public class AdminNotificationObserver implements ReservationObserver {
 
 @Override
 public void update(Reservation reservation, String evenementType) {
     System.out.println("[Observer] Notification à l'administrateur: " + 
                        "Réservation " + reservation.getId() + " - " + evenementType);
     
 }
}