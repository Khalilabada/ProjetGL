package com.boky.PFE.service;

import com.boky.PFE.Beans.SavePlanification;
import com.boky.PFE.entite.Planification;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.repository.PlanificationRepository;
import com.boky.PFE.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PlanificationServiceImpl implements PlanificationService
{

    private static final int MAX_PLANIFICATIONS_MEME_JOUR_PAR_FDM = 30;

    @Autowired
    PlanificationRepository planificationRepository;
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Override
    public Planification AjouterPlanification (SavePlanification model)    {
        assertPrixParHeureStrictementPositif(model.getPrixParHeure());
        if (!utilisateurRepository.existsById(model.getId_fdm())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Aucun utilisateur (FDM) ne correspond à id_fdm.");
        }
        assertCapaciteJourFdm(model.getId_fdm(), model.getJour(), null);

        Planification planification= SavePlanification.toEntity(model);
        System.out.println("idFDM"+model.getId_fdm());
        Utilisateur utilisateur = utilisateurRepository.findById(model.getId_fdm())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Aucun utilisateur (FDM) ne correspond à id_fdm."));
        planification.setFdm(utilisateur);
        return planificationRepository.save(planification);
    }

    @Override
    public Planification ModifierPlanification(Planification planification) {
        Utilisateur fmd = this.FdmByPlanning(planification.getId());
        planification.setFdm(fmd);
        if (planification.getJour() == null || planification.getJour().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le jour est obligatoire.");
        }
        assertPrixParHeureStrictementPositif(planification.getPrixParHeure());
        assertCapaciteJourFdm(fmd.getId(), planification.getJour(), planification.getId());
        return planificationRepository.save(planification);
    }

    @Override
    public List<Planification> AfficherPlanification() {
        return planificationRepository.findAll();
    }

    @Override
    public void SupprimerPlanification(Long id) {
        planificationRepository.deleteById(id);
    }

    @Override
    public Optional<Planification> getPlanificationById(Long id) {
        return planificationRepository.findById(id);
    }
@Override
    public List<Planification> listePlanificationByFdm(Long id) {
        return planificationRepository.findByFdmId(id);
    }
@Override
    public Utilisateur FdmByPlanning(  Long id) {
        Optional<Planification> planification =  planificationRepository.findById(id);
        return planification.get().getFdm();
    }

    private static void assertPrixParHeureStrictementPositif(String raw) {
        // OCL: prixParHeure <> null and prixParHeure <> ''
        if (raw == null || raw.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le prix par heure est obligatoire.");
        }
        // OCL: prixParHeure <> null and prixParHeure <> ''
        String normalized = raw.trim().replace(',', '.');
        BigDecimal prix;
        try {
            prix = new BigDecimal(normalized);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le prix par heure doit être un nombre valide.");
        }
        if (prix.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le prix par heure doit être strictement positif.");
        }
    }

    private void assertCapaciteJourFdm(Long fdmId, String jour, Long excludePlanificationId) {
        long count = excludePlanificationId == null
                ? planificationRepository.countByFdmIdAndJour(fdmId, jour)
                : planificationRepository.countByFdmIdAndJourAndIdNot(fdmId, jour, excludePlanificationId);
        if (count >= MAX_PLANIFICATIONS_MEME_JOUR_PAR_FDM) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nombre maximal de planifications atteint pour ce formateur ce jour ("
                            + MAX_PLANIFICATIONS_MEME_JOUR_PAR_FDM + ").");
        }
    }
}
