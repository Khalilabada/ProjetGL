package com.boky.PFE.service;

import com.boky.PFE.Beans.SavereservationFM;
import com.boky.PFE.entite.*;
import com.boky.PFE.repository.ReservationFMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationFMServiceImpl implements ReservationFMService {
    
    @Autowired
    PlanificationService planificationService;

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    ReservationFMRepository reservationFMRepository;

    @Autowired
    EmailService emailService;

    @Override
    public ReservationFM AjouterReservationFM(SavereservationFM model) {
        System.out.println("[ReservationFMService] Création d'une nouvelle réservation");
        
        ReservationFM reservationFM = SavereservationFM.toEntity(model);

        Optional<Planification> planificationOpt = planificationService.getPlanificationById(model.getId_planification());
        Optional<Utilisateur> utilisateurClient = utilisateurService.getUtilisateurById(model.getId_client());

        if (planificationOpt.isPresent() && utilisateurClient.isPresent()) {
            
            Planification planning = planificationOpt.get();
            
            // ========== NOUVEAU CODE : Vérifier si le planning peut être réservé ==========
            if (!planning.peutReserver()) {
                System.out.println("[State] Impossible de réserver : planning en état " + planning.getNomEtat());
                return null;
            }
            
            reservationFM.setUtilisateur(utilisateurClient.get());
            reservationFM.setPlanification(planning);
            
            // ========== NOUVEAU CODE : Changer l'état du planning vers RÉSERVÉ ==========
            planning.reserver();
            planificationService.ModifierPlanification(planning);

            // Envoi d'email existant
            emailService.SendSimpleMessage(
                    planning.getHeureDisponible(),
                    "Nouvelle réservation pour votre planning",
                    "Bonjour,\n\n" +
                            "Nous vous informons que votre planning a été réservé. " +
                            "Veuillez consulter votre profil pour confirmer la réservation.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe de gestion des réservations"
            );

            ReservationFM savedReservation = reservationFMRepository.save(reservationFM);
            System.out.println("[ReservationFMService] Réservation créée avec succès. ID: " + savedReservation.getId());
            return savedReservation;
        } else {
            System.err.println("[ReservationFMService] Erreur: Planning ou utilisateur non trouvé");
            return null;
        }
    }

    @Override
    public List<ReservationFM> AfficherReservationFM() {
        return reservationFMRepository.findAll();
    }

    @Override
    public List<ReservationFM> listeReservationFMByUtilisateur(Long id) {
        return reservationFMRepository.findByUtilisateurId(id);
    }
    
    @Override
    public List<ReservationFM> listeReservationFMByPlanning(Long id) {
        return reservationFMRepository.findByPlanificationId(id);
    }

    @Override
    public Utilisateur ClientByReservationFM(Long id) {
        Optional<ReservationFM> reservationFM = reservationFMRepository.findById(id);
        if (reservationFM.isPresent()) {
            return reservationFM.get().getUtilisateur();
        } else {
            throw new NoSuchElementException("ReservationFM non trouvée avec l'id: " + id);
        }
    }

    @Override
    public Planification planificationByReservationFM(Long id) {
        Optional<ReservationFM> reservationFM = reservationFMRepository.findById(id);
        if (reservationFM.isPresent()) {
            return reservationFM.get().getPlanification();
        } else {
            throw new NoSuchElementException("ReservationFM non trouvée avec l'id: " + id);
        }
    }

    @Override
    public ReservationFM ModifierReservationFM(ReservationFM reservationFM) {
        System.out.println("[ReservationFMService] Modification de la réservation ID: " + reservationFM.getId());
        
        Utilisateur client = this.ClientByReservationFM(reservationFM.getId());
        Planification planification = this.planificationByReservationFM(reservationFM.getId());

        reservationFM.setUtilisateur(client);
        reservationFM.setPlanification(planification);

        Optional<ReservationFM> reservationFMOptional = this.getReservationFMById(reservationFM.getId());
        if (!reservationFMOptional.isPresent()) {
            throw new NoSuchElementException("Reservation non trouvée avec l'id: " + reservationFM.getId());
        }

        reservationFM.setEtat(true);

        String etat = reservationFM.isConfirmation() ? "acceptée" : "non confirmée";

        emailService.SendSimpleMessage(
                client.getEmail(),
                "Réponse concernant votre réservation de ménage",
                "Bonjour,\n\n" +
                        "Nous souhaitons vous informer que votre réservation pour le service de ménage a été " + etat + " pour la date du " + planification.getJour() + ".\n\n" +
                        "Merci de consulter votre profil pour plus de détails.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe de gestion des réservations"
        );

        ReservationFM updatedReservation = reservationFMRepository.save(reservationFM);
        System.out.println("[ReservationFMService] Réservation modifiée avec succès");
        return updatedReservation;
    }
    
    // ========== MÉTHODES POUR LE PATTERN STATE ==========
    
    public void demarrerTravail(Long planificationId) {
        System.out.println("[ReservationFMService] Démarrage du travail pour planning #" + planificationId);
        
        Optional<Planification> planningOpt = planificationService.getPlanificationById(planificationId);
        
        if (planningOpt.isPresent()) {
            Planification planning = planningOpt.get();
            
            if (planning.peutCommencer()) {
                planning.commencer();
                planificationService.ModifierPlanification(planning);
                System.out.println("[State] Travail démarré pour planning #" + planificationId + " - Nouvel état: " + planning.getNomEtat());
            } else {
                System.out.println("[State] Impossible de démarrer : planning en état " + planning.getNomEtat());
            }
        } else {
            System.err.println("[ReservationFMService] Planning non trouvé");
        }
    }
    
    public void terminerTravail(Long planificationId) {
        System.out.println("[ReservationFMService] Fin du travail pour planning #" + planificationId);
        
        Optional<Planification> planningOpt = planificationService.getPlanificationById(planificationId);
        
        if (planningOpt.isPresent()) {
            Planification planning = planningOpt.get();
            
            if (planning.peutTerminer()) {
                planning.terminer();
                planificationService.ModifierPlanification(planning);
                System.out.println("[State] Travail terminé pour planning #" + planificationId + " - Nouvel état: " + planning.getNomEtat());
            } else {
                System.out.println("[State] Impossible de terminer : planning en état " + planning.getNomEtat());
            }
        } else {
            System.err.println("[ReservationFMService] Planning non trouvé");
        }
    }
    
    public void annulerPlanning(Long planificationId) {
        System.out.println("[ReservationFMService] Annulation du planning #" + planificationId);
        
        Optional<Planification> planningOpt = planificationService.getPlanificationById(planificationId);
        
        if (planningOpt.isPresent()) {
            Planification planning = planningOpt.get();
            
            if (planning.peutAnnuler()) {
                planning.annuler();
                planificationService.ModifierPlanification(planning);
                System.out.println("[State] Planning annulé #" + planificationId + " - Nouvel état: " + planning.getNomEtat());
            } else {
                System.out.println("[State] Impossible d'annuler : planning en état " + planning.getNomEtat());
            }
        } else {
            System.err.println("[ReservationFMService] Planning non trouvé");
        }
    }

    @Override
    public Optional<ReservationFM> getReservationFMById(Long id) {
        return reservationFMRepository.findById(id);
    }
    
    @Override
    public List<ReservationFM> listReservationByFDM(Long idFDM) {
        List<Planification> planifications = planificationService.listePlanificationByFdm(idFDM);
        List<ReservationFM> reservations = new ArrayList<>();

        for (Planification planification : planifications) {
            List<ReservationFM> reservationsPlanification = reservationFMRepository.findByPlanificationId(planification.getId());
            reservations.addAll(reservationsPlanification);
        }
        return reservations;
    }
    
    @Override
    public void SupprimerReservationFDM(Long id) {
        System.out.println("[ReservationFMService] Suppression de la réservation ID: " + id);
        reservationFMRepository.deleteById(id);
    }
}