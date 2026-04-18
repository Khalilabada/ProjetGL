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
     // Ces emails sont DÉJÀ envoyés par l'ancien code
     // Mais Observer permet d'en ajouter d'autres sans modifier le service
     
     switch(evenementType) {
         case "CREATE":
             // Email à l'annonceur (déjà fait par l'ancien code)
             System.out.println("[Observer] Notification email pour création");
             break;
         case "CONFIRM":
             // Email au client (déjà fait par l'ancien code)
             System.out.println("[Observer] Notification email pour confirmation");
             break;
     }
 }
}