package com.boky.PFE.restController;

import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.service.EvaluationFDMService;
import com.boky.PFE.service.UtilisateurService;
import com.boky.PFE.util.NewPassword;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Utilisateur")
public class UtilisateurRestController {

    private final UtilisateurService utilisateurService;
    private final EvaluationFDMService evaluationFDMService;

    public UtilisateurRestController(
            UtilisateurService utilisateurService,
            EvaluationFDMService evaluationFDMService) {
        this.utilisateurService = utilisateurService;
        this.evaluationFDMService = evaluationFDMService;
    }

    @PostMapping(value = "/register")
    ResponseEntity<?> AjouterUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.AjouterUtilisateur(utilisateur);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Utilisateur> AfficherUtilisateur() {
        return utilisateurService.AfficherUtilisateur();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void SupprimerUtilisateur(@PathVariable("id") Long id) {
        evaluationFDMService.supprimerEvaluationFDMParFDM(id);
        utilisateurService.SupprimerUtilisateur(id);
    }

    @PostMapping("/Login")
    public ResponseEntity<Map<String, Object>> loginUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.loginUtilisateur(utilisateur);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Utilisateur ModifierUtilisateur(@RequestBody Utilisateur utilisateur, @PathVariable("id") Long id) {
        return utilisateurService.updateUtilisateur(id, utilisateur).orElse(null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Utilisateur> getUtilisateurById(@PathVariable("id") long id) {
        return utilisateurService.getUtilisateurById(id);
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        return utilisateurService.ConfirmationEmail(confirmationToken);
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<Map<String, Object>> resetPasswordEmail(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.requestPasswordResetCode(utilisateur);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody NewPassword newPassword) {
        return utilisateurService.resetPasswordWithCode(newPassword);
    }

    @RequestMapping("/send_email")
    public ResponseEntity<Map<String, Object>> SendEmail(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.sendAnnonceReminderEmail(utilisateur);
    }

    @GetMapping("/role")
    public List<Utilisateur> getUtilisateurByRole(@RequestParam String role) {
        return utilisateurService.getUtilisateurListByRole(role);
    }

    @GetMapping("/email")
    public Utilisateur getUtilisateurByEmail(@RequestParam String email) {
        return utilisateurService.getUtilisateurByEmail(email);
    }
}
