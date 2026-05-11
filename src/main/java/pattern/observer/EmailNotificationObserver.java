package pattern.observer;

//nouveau fichier: com.boky.PFE.pattern.observer.EmailNotificationObserver

import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationObserver implements ReservationObserver {
 
 @Autowired
 private EmailService emailService;
 
 @Override
 public void update(Reservation reservation, String evenementType) {
     
     switch(evenementType) {
         case "CREATE":
             System.out.println("[Observer] Notification email pour création");
             break;
         case "CONFIRM":
             System.out.println("[Observer] Notification email pour confirmation");
             break;
     }
 }
}