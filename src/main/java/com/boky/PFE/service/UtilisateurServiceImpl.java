package com.boky.PFE.service;

import com.boky.PFE.entite.ConfirmationToken;
import com.boky.PFE.entite.Utilisateur;
import com.boky.PFE.repository.ConfirmationTokenRepository;
import com.boky.PFE.repository.UtilisateurRepository;
import com.boky.PFE.util.NewPassword;
import com.boky.PFE.util.UserCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtilisateurService emailUtilisateurService;
    private final NotificationService notificationService;
    private final JwtTokenService jwtTokenService;

    public UtilisateurServiceImpl(
            UtilisateurRepository utilisateurRepository,
            ConfirmationTokenRepository confirmationTokenRepository,
            PasswordEncoder passwordEncoder,
            EmailUtilisateurService emailUtilisateurService,
            NotificationService notificationService,
            JwtTokenService jwtTokenService) {
        this.utilisateurRepository = utilisateurRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailUtilisateurService = emailUtilisateurService;
        this.notificationService = notificationService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public ResponseEntity<Object> AjouterUtilisateur(Utilisateur utilisateur) {
        Utilisateur existingUser = utilisateurRepository.findByEmail(utilisateur.getEmail());
        HashMap<String, Object> response = new HashMap<>();
        if (existingUser!=null) {
            response.put("message", "Email is already in use!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        }
        utilisateur.setMdp(this.passwordEncoder.encode(utilisateur.getMdp()));
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
        return utilisateurRepository.findUtilisateursByRole(role);
    }

    @Override
    public Utilisateur findUtilisateurByEmail(String email) {
        return utilisateurRepository.findUtilisateurByEmail(email);
    }

    @Override
    public Utilisateur getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Override
    public List<Utilisateur> getUtilisateurListByRole(String role) {
        return utilisateurRepository.findUtilisateurByRole(role);
    }

    @Override
    public Optional<Utilisateur> updateUtilisateur(Long id, Utilisateur utilisateur) {
        Optional<Utilisateur> opt = utilisateurRepository.findById(id);
        if (opt.isEmpty()) {
            return Optional.empty();
        }
        Utilisateur utilisateur1 = opt.get();
        utilisateur1.setId(utilisateur.getId());
        utilisateur1.setNom(utilisateur.getNom());
        utilisateur1.setPrenom(utilisateur.getPrenom());
        utilisateur1.setEmail(utilisateur.getEmail());
        utilisateur1.setDate_de_naissance(utilisateur.getDate_de_naissance());
        utilisateur1.setTelephone(utilisateur.getTelephone());
        utilisateur1.setAdresse(utilisateur.getAdresse());
        utilisateur1.setRole(utilisateur.getRole());
        utilisateur1.setPhoto(utilisateur.getPhoto());
        if (utilisateur.isEtat() != utilisateur1.isEtat()) {
            notificationService.notifyAccountStateChanged(utilisateur1, utilisateur.isEtat());
        }
        utilisateur1.setEtat(utilisateur.isEtat());
        utilisateur1.setMdp(passwordEncoder.encode(utilisateur.getMdp()));
        return Optional.of(utilisateurRepository.save(utilisateur1));
    }

    @Override
    public ResponseEntity<Map<String, Object>> loginUtilisateur(Utilisateur utilisateur) {
        HashMap<String, Object> response = new HashMap<>();
        Utilisateur userFromDB = utilisateurRepository.findUtilisateurByEmail(utilisateur.getEmail());
        if (userFromDB == null) {
            response.put("message", "Utilisateur not found !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (!passwordEncoder.matches(utilisateur.getMdp(), userFromDB.getMdp())) {
            response.put("message", "Incorrect password !");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        if (!userFromDB.isEtat()) {
            response.put("message", "Account is not activated !");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String token = jwtTokenService.createTokenForUserData(userFromDB);
        response.put("token", token);
        response.put("role", userFromDB.getRole());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Map<String, Object>> requestPasswordResetCode(Utilisateur utilisateur) {
        HashMap<String, Object> response = new HashMap<>();
        Utilisateur user = utilisateurRepository.findUtilisateurByEmail(utilisateur.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        String code = UserCode.getCode();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(utilisateur.getEmail());
        mailMessage.setSubject("Code de réinitialisation de mot de passe");
        mailMessage.setText("Votre code : " + code);
        emailUtilisateurService.sendEmail(mailMessage);
        user.getCode().setCode(code);
        ModifierUtilisateur(user, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Map<String, Object>> resetPasswordWithCode(NewPassword newPassword) {
        HashMap<String, Object> response = new HashMap<>();
        Utilisateur user = utilisateurRepository.findUtilisateurByEmail(newPassword.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (!user.getCode().getCode().equals(newPassword.getCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        user.setMdp(passwordEncoder.encode(newPassword.getPassword()));
        ModifierUtilisateur(user, user.getId());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Réinitialisation de mot de passe");
        mailMessage.setText("Votre mot de passe a été changé avec succès !");
        emailUtilisateurService.sendEmail(mailMessage);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Map<String, Object>> sendAnnonceReminderEmail(Utilisateur utilisateur) {
        HashMap<String, Object> response = new HashMap<>();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(utilisateur.getEmail());
        mailMessage.setSubject("Finalisez la mise en ligne de votre annonce");
        mailMessage.setText(
                "Bonjour,\n\n"
                        + "Nous vous remercions d'avoir choisi notre plateforme pour publier votre annonce. "
                        + "Il ne vous reste plus que quelques détails à confirmer pour finaliser la mise en ligne de votre annonce.\n\n"
                        + "En terminant ces étapes rapidement, vous permettrez aux voyageurs de commencer à réserver dès que possible. "
                        + "Nous vous encourageons à ne pas attendre pour maximiser vos chances de recevoir des réservations.\n\n"
                        + "Si vous avez besoin d'aide ou de plus d'informations, n'hésitez pas à nous contacter.\n\n"
                        + "Cordialement,\n"
        );
        emailUtilisateurService.sendEmail(mailMessage);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
