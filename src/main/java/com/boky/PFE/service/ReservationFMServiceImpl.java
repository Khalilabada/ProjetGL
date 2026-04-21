package com.boky.PFE.service;

import com.boky.PFE.Beans.SavereservationFM;
import com.boky.PFE.entite.*;
import com.boky.PFE.repository.ReservationFMRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationFMServiceImpl implements ReservationFMService {

    private final PlanificationService planificationService;
    private final UtilisateurService utilisateurService;
    private final ReservationFMRepository reservationFMRepository;
    private final NotificationService notificationService;

    public ReservationFMServiceImpl(
            PlanificationService planificationService,
            UtilisateurService utilisateurService,
            ReservationFMRepository reservationFMRepository,
            NotificationService notificationService) {
        this.planificationService = planificationService;
        this.utilisateurService = utilisateurService;
        this.reservationFMRepository = reservationFMRepository;
        this.notificationService = notificationService;
    }

    @Override
    public ReservationFM AjouterReservationFM(SavereservationFM model) {
        ReservationFM reservationFM = SavereservationFM.toEntity(model);

        Optional<Planification> planification = planificationService.getPlanificationById(model.getId_planification());
        Optional<Utilisateur> utilisateurClient = utilisateurService.getUtilisateurById(model.getId_client());

        if (planification.isPresent() && utilisateurClient.isPresent()) {
            reservationFM.setUtilisateur(utilisateurClient.get());
            reservationFM.setPlanification(planification.get());
            notificationService.notifyReservationFMCreated(planification.get());

            return reservationFMRepository.save(reservationFM);
        } else {
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
        Utilisateur client = this.ClientByReservationFM(reservationFM.getId());
        Planification planification = this.planificationByReservationFM(reservationFM.getId());

        reservationFM.setUtilisateur(client);
        reservationFM.setPlanification(planification);

        Optional<ReservationFM> reservationFMOptional = this.getReservationFMById(reservationFM.getId());
        if (!reservationFMOptional.isPresent()) {
            throw new NoSuchElementException("Reservation non trouvée avec l'id: " + reservationFM.getId());
        }

        reservationFM.setEtat(true);
        notificationService.notifyReservationFMResponse(client, planification, reservationFM);

        return reservationFMRepository.save(reservationFM);
    }

    @Override
    public Optional<ReservationFM> getReservationFMById(Long id) {
        return reservationFMRepository.findById(id);
    }
    @Override
    public List<ReservationFM> listReservationByFDM(Long idFDM) {
        // Récupérer toutes les planifications pour le FDM donné
        List<Planification> planifications = planificationService.listePlanificationByFdm(idFDM);
        List<ReservationFM> reservations = new ArrayList<>();

        // Pour chaque planification, récupérer les réservations associées
        for (Planification planification : planifications) {
            List<ReservationFM> reservationsPlanification = reservationFMRepository.findByPlanificationId(planification.getId());
            reservations.addAll(reservationsPlanification);
        }

        return reservations;
    }
    @Override
    public void SupprimerReservationFDM(Long id){
        reservationFMRepository.deleteById(id);
    }
}
