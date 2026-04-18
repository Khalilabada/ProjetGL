package pattern.observer;


import java.time.LocalDateTime;

public class LogObserver {
    public void update() {
        System.out.println("[LOG] " + LocalDateTime.now() + " - Réservation créée");
    }
}