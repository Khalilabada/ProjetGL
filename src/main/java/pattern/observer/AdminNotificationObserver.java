package pattern.observer;

//nouveau fichier: com.boky.PFE.pattern.observer.AdminNotificationObserver

import com.boky.PFE.entite.Reservation;
import org.springframework.stereotype.Component;

@Component
public class AdminNotificationObserver implements ReservationObserver {
 
 @Override
 public void update(Reservation reservation, String evenementType) {
     // NOUVELLE FONCTIONNALITÉ : notifier l'admin
     System.out.println("[Observer] Notification à l'administrateur: " + 
                        "Réservation " + reservation.getId() + " - " + evenementType);
     
     // Ici vous pouvez ajouter un email à l'admin ou un log
 }
}