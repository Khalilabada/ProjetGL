package com.boky.PFE.service;

import com.boky.PFE.Beans.SavereservationFM;
import com.boky.PFE.entite.Planification;
import com.boky.PFE.entite.ReservationFM;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.factory.FactoryProvider;
import com.boky.PFE.factory.ServiceFactory;
import com.boky.PFE.factory.reservation.IReservation;
import com.boky.PFE.repository.ReservationFMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationFMServiceImpl extends BaseService implements ReservationFMService {
    @Autowired
    private FactoryProvider factoryProvider;

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

        IReservation iReservation = createReservation(model.getType());
    iReservation.remplirDepuisFM(model); 

    Optional<Planification> planification = planificationService.getPlanificationById(model.getId_planification());
    Optional<Utilisateur> utilisateurClient = utilisateurService.getUtilisateurById(model.getId_client());

    if (planification.isPresent() && utilisateurClient.isPresent()) {
        ReservationFM res = (ReservationFM) iReservation;
        res.setUtilisateur(utilisateurClient.get());
        res.setPlanification(planification.get());

        sendEmail(utilisateurClient.get().getEmail(), iReservation, planification.get().getJour());

        return reservationFMRepository.save(res);
    }
    return null;
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
        return reservationFMRepository.save(reservationFM);
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
        reservationFMRepository.deleteById(id);
    }
}
