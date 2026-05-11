package com.boky.PFE.service;

import com.boky.PFE.Beans.UtilisateurRequest;
import com.boky.PFE.entite.ConfirmationToken;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.factory.UtilisateurFactoryProvider;
import com.boky.PFE.repository.ConfirmationTokenRepository;
import com.boky.PFE.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImpl implements UtilisateurService
{
    @Autowired
    UtilisateurRepository utilisateurRepository;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;


    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    EmailUtilisateurService emailUtilisateurService;
    @Autowired
    EmailService emailService;

    @Override
    public ResponseEntity<Object> AjouterUtilisateur(Utilisateur utilisateur) {
        Utilisateur existingUser = utilisateurRepository.findByEmail(utilisateur.getEmail());
        HashMap<String, Object> response = new HashMap<>();
        if (existingUser!=null) {
            response.put("message", "Email is already in use!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        }
        //  client.setMdp(this.bCryptPasswordEncoder.encode(client.getMdp()));
        utilisateur.setMdp(this.bCryptPasswordEncoder.encode(utilisateur.getMdp()));
        utilisateurRepository.save(utilisateur);
        ConfirmationToken confirmationToken = new ConfirmationToken(utilisateur);
        confirmationTokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(utilisateur.getEmail());
        mailMessage.setSubject("Confirmation de votre inscription");

        String message = "Bonjour " + utilisateur.getPrenom() + " " + utilisateur.getNom() + ",\n\n" +
                "Merci de vous être inscrit sur notre site. Pour compléter votre inscription, veuillez confirmer votre compte en cliquant sur le lien ci-dessous :\n\n" +
                "http://localhost:8081/api/Utilisateur/confirm-account?token=" + confirmationToken.getConfirmationToken() + "\n\n" +
                "Si vous n'avez pas demandé cette inscription, veuillez ignorer cet email.\n\n" +
                "Cordialement,\n" +
                "L'équipe de support";

        mailMessage.setText(message);
        emailUtilisateurService.sendEmail(mailMessage);

        System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());

        return ResponseEntity.ok("Verify email by the link sent on your email address");
    }

    @Override
    public Utilisateur ModifierUtilisateur(Utilisateur utilisateur, long id) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur modifierDepuisRequete(Long id, UtilisateurRequest request) {
        Optional<Utilisateur> optional = utilisateurRepository.findById(id);
        if (optional.isEmpty()) {
            return null;
        }
        Utilisateur u = optional.get();
        if (!UtilisateurFactoryProvider.typeCorrespondAuCompte(u, request.getType())) {
            throw new IllegalArgumentException("Le type ne correspond pas a ce compte.");
        }
        if (request.getId() != null) {
            u.setId(request.getId());
        }
        if (request.getNom() != null) {
            u.setNom(request.getNom());
        }
        if (request.getPrenom() != null) {
            u.setPrenom(request.getPrenom());
        }
        if (request.getEmail() != null) {
            u.setEmail(request.getEmail());
        }
        if (request.getDate_de_naissance() != null) {
            u.setDate_de_naissance(request.getDate_de_naissance());
        }
        if (request.getTelephone() != null) {
            u.setTelephone(request.getTelephone());
        }
        if (request.getAdresse() != null) {
            u.setAdresse(request.getAdresse());
        }
        if (request.getPhoto() != null) {
            u.setPhoto(request.getPhoto());
        }
        if (request.getMdp() != null && !request.getMdp().isEmpty()) {
            if (!bCryptPasswordEncoder.matches(request.getMdp(), u.getMdp())) {
                u.setMdp(bCryptPasswordEncoder.encode(request.getMdp()));
            }
        }
        if (request.isEtat() != u.isEtat()) {
            String etat = u.isEtat() ? "Bloqué" : "Accepté";
            emailService.SendSimpleMessage(u.getEmail(), "L'etat de votre compte", "votre compte a été " + etat);
        }
        u.setEtat(request.isEtat());
        return utilisateurRepository.save(u);
    }

    @Override
    public List<Utilisateur> AfficherUtilisateur() {
        System.out.println("wsol lhna");
        return utilisateurRepository.findAll();
    }

    @Override
    public void SupprimerUtilisateur(Long idUtilisateur) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findById(idUtilisateur);

            Utilisateur utilisateur = optionalUtilisateur.get();
            ConfirmationToken token = confirmationTokenRepository.findByUtilisateur(utilisateur);
            if (token != null) {
                confirmationTokenRepository.delete(token);
            }
            utilisateurRepository.delete(utilisateur);

    }

    @Override
    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }





    @Override
    public ResponseEntity<?> ConfirmationEmail(String confirmationEmail) {

        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationEmail);

        if(token != null)
        {

            Utilisateur utilisateur = utilisateurRepository.findByEmail(token.getUtilisateur().getEmail());
            System.out.println("email from token " +token.getUtilisateur().getEmail());
            utilisateur.setEtat(true);
            utilisateurRepository.save(utilisateur);
            return ResponseEntity.ok("Email verified successfully! "+"http://localhost:4200/login"  );
        }

        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }
    @Override
public List<Utilisateur> getUtilisateurByRole(String role) {
    if (role == null || role.isBlank()) {
        return utilisateurRepository.findAll();
    }

    String normalizedRole = role.trim().toLowerCase();
    return utilisateurRepository.findAll().stream()
            .filter(utilisateur -> utilisateur.getClass().getSimpleName().toLowerCase().equals(normalizedRole))
            .collect(Collectors.toList());
}

}
