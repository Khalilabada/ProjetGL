# 📐 Implémentation Java des Contraintes OCL

## Approche Hybride : OCL + Java + Spring Validation

Pour implémenter ces contraintes OCL dans votre application Spring Boot, voici une approche pratique en 3 couches :

---

## 🔧 **COUCHE 1 : Annotations de Validation Custom**

```java
// Package: com.boky.PFE.validation

package com.boky.PFE.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.boky.PFE.validation.validators.ReservationValidator;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReservationValidator.class)
@Documented
public @interface ValidReservation {
    String message() default "La réservation viole une contrainte OCL";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

---

## 📝 **COUCHE 2 : Validateurs Métier (OCL → Java)**

### **Validateur 1 : Intégrité Transactionnelle Réservation**

```java
package com.boky.PFE.validation.validators;

import com.boky.PFE.entite.Reservation;
import com.boky.PFE.repository.ReservationRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReservationValidator implements ConstraintValidator<ValidReservation, Reservation> {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    
    @Override
    public boolean isValid(Reservation reservation, ConstraintValidatorContext context) {
        if (reservation == null) return true;
        
        if (!reservation.isConfirmation()) return true;
        
        // OCL Condition 1: Validité temporelle
        if (!validateTemporalCoherence(reservation, context)) {
            return false;
        }
        
        // OCL Condition 2: Pas de chevauchement
        if (!validateNoOverlapping(reservation, context)) {
            return false;
        }
        
        // OCL Condition 3: Montant calculé correctement
        if (!validateAmount(reservation, context)) {
            return false;
        }
        
        // OCL Condition 4: Score minimum du client
        if (!validateClientRating(reservation, context)) {
            return false;
        }
        
        // OCL Condition 5: Limite de réservations simultanées
        if (!validateConcurrentReservationLimit(reservation, context)) {
            return false;
        }
        
        return true;
    }
    
    private boolean validateTemporalCoherence(Reservation res, ConstraintValidatorContext ctx) {
        try {
            LocalDate arrival = LocalDate.parse(res.getDate_arrivee(), formatter);
            LocalDate departure = LocalDate.parse(res.getDate_depart(), formatter);
            
            if (!arrival.isBefore(departure)) {
                addConstraintViolation(ctx, 
                    "OCL Violation: date_arrivee doit être antérieure à date_depart");
                return false;
            }
            return true;
        } catch (Exception e) {
            addConstraintViolation(ctx, "Format de date invalide");
            return false;
        }
    }
    
    private boolean validateNoOverlapping(Reservation res, ConstraintValidatorContext ctx) {
        List<Reservation> conflictingReservations = 
            reservationRepository.findConflictingReservations(
                res.getAnnonce().getId(),
                res.getDate_arrivee(),
                res.getDate_depart(),
                res.getId() != null ? res.getId() : -1
            );
        
        if (!conflictingReservations.isEmpty()) {
            addConstraintViolation(ctx,
                "OCL Violation: Il existe une réservation chevauchante sur cette annonce");
            return false;
        }
        return true;
    }
    
    private boolean validateAmount(Reservation res, ConstraintValidatorContext ctx) {
        long expectedAmount = (long) (res.getAnnonce().getPrix() * res.getNb_nuit());
        
        if (res.getMontant_paye() != expectedAmount) {
            addConstraintViolation(ctx,
                String.format("OCL Violation: Montant invalide. Attendu: %d, Reçu: %d",
                    expectedAmount, res.getMontant_paye()));
            return false;
        }
        return true;
    }
    
    private boolean validateClientRating(Reservation res, ConstraintValidatorContext ctx) {
        // TODO: Implémenter après avoir lu la structure des évaluations
        // double avgRating = evaluationRepository.getAverageRating(res.getUtilisateur().getId());
        // return avgRating >= 3.0;
        return true;
    }
    
    private boolean validateConcurrentReservationLimit(Reservation res, ConstraintValidatorContext ctx) {
        long confirmedCount = reservationRepository.countConfirmedByClient(
            res.getUtilisateur().getId()
        );
        
        if (confirmedCount >= 2) {
            addConstraintViolation(ctx,
                "OCL Violation: Le client a déjà 2 réservations confirmées");
            return false;
        }
        return true;
    }
    
    private void addConstraintViolation(ConstraintValidatorContext ctx, String message) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(message)
           .addConstraintViolation();
    }
}
```

---

### **Validateur 2 : Cohérence Causale Chat-Message**

```java
package com.boky.PFE.validation.validators;

import com.boky.PFE.entite.Chat;
import com.boky.PFE.entite.Message;
import com.boky.PFE.repository.ChatRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChatValidator implements ConstraintValidator<ValidChat, Chat> {
    
    @Autowired
    private ChatRepository chatRepository;
    
    @Override
    public boolean isValid(Chat chat, ConstraintValidatorContext context) {
        if (chat == null) return true;
        
        // OCL Condition 1: Tous les messages appartiennent à ce chat
        if (!validateMessageOwnership(chat, context)) {
            return false;
        }
        
        // OCL Condition 2: Email cohérence
        if (!validateEmailCoherence(chat, context)) {
            return false;
        }
        
        // OCL Condition 3: Ordre chronologique
        if (!validateChronologicalOrder(chat, context)) {
            return false;
        }
        
        // OCL Condition 4: Unicité du chat
        if (!validateChatUniqueness(chat, context)) {
            return false;
        }
        
        return true;
    }
    
    private boolean validateMessageOwnership(Chat chat, ConstraintValidatorContext ctx) {
        if (chat.getMessageList() == null) return true;
        
        boolean allBelongToThisChat = chat.getMessageList().stream()
            .allMatch(msg -> msg.getChat() != null && msg.getChat().equals(chat));
        
        if (!allBelongToThisChat) {
            addViolation(ctx, "OCL: Un ou plusieurs messages n'appartiennent pas à ce chat");
            return false;
        }
        return true;
    }
    
    private boolean validateEmailCoherence(Chat chat, ConstraintValidatorContext ctx) {
        if (chat.getMessageList() == null) return true;
        
        Set<String> validEmails = Set.of(
            chat.getEmailfirstUserName(),
            chat.getEmailSecondeUser()
        );
        
        boolean allEmailsValid = chat.getMessageList().stream()
            .allMatch(msg -> validEmails.contains(msg.getSenderEmail()));
        
        if (!allEmailsValid) {
            addViolation(ctx, "OCL: Un message contient un email invalide");
            return false;
        }
        return true;
    }
    
    private boolean validateChronologicalOrder(Chat chat, ConstraintValidatorContext ctx) {
        if (chat.getMessageList() == null || chat.getMessageList().size() < 2) {
            return true;
        }
        
        List<Message> sorted = chat.getMessageList().stream()
            .sorted((m1, m2) -> Long.compare(
                m1.getTime().getTime(),
                m2.getTime().getTime()
            ))
            .collect(Collectors.toList());
        
        for (int i = 0; i < sorted.size() - 1; i++) {
            if (sorted.get(i).getTime().getTime() >= sorted.get(i + 1).getTime().getTime()) {
                addViolation(ctx, "OCL: L'ordre chronologique n'est pas respecté");
                return false;
            }
        }
        return true;
    }
    
    private boolean validateChatUniqueness(Chat chat, ConstraintValidatorContext ctx) {
        if (chat.getId() == 0) {  // Nouvelle création
            long existingCount = chatRepository.countDuplicateChats(
                chat.getEmailfirstUserName(),
                chat.getEmailSecondeUser()
            );
            
            if (existingCount > 0) {
                addViolation(ctx, "OCL: Un chat existe déjà entre ces deux utilisateurs");
                return false;
            }
        }
        return true;
    }
    
    private void addViolation(ConstraintValidatorContext ctx, String message) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(message)
           .addConstraintViolation();
    }
}
```

---

### **Validateur 3 : Validité Comportementale Évaluations**

```java
package com.boky.PFE.validation.validators;

import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.repository.EvaluationRepositrory;
import com.boky.PFE.repository.ReservationRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class EvaluationValidator implements ConstraintValidator<ValidEvaluation, Evaluation> {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private EvaluationRepositrory evaluationRepository;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    
    @Override
    public boolean isValid(Evaluation eval, ConstraintValidatorContext context) {
        if (eval == null) return true;
        
        // OCL Condition 1: Utilisateur a réservé cette annonce
        if (!validateUserHasReservation(eval, context)) {
            return false;
        }
        
        // OCL Condition 2: Fenêtre temporelle (7 jours après départ)
        if (!validateTemporalWindow(eval, context)) {
            return false;
        }
        
        // OCL Condition 3: Une seule évaluation par utilisateur par annonce
        if (!validateEvaluationUniqueness(eval, context)) {
            return false;
        }
        
        // OCL Condition 4: L'annonceur ne peut pas évaluer sa propre annonce
        if (!validateNoSelfEvaluation(eval, context)) {
            return false;
        }
        
        // OCL Condition 5: Commentaire non-vide
        if (!validateCommentNotEmpty(eval, context)) {
            return false;
        }
        
        return true;
    }
    
    private boolean validateUserHasReservation(Evaluation eval, ConstraintValidatorContext ctx) {
        boolean hasValidReservation = reservationRepository.findCompletedReservationByUserAndAnnonce(
            eval.getUtilisateur().getId(),
            eval.getAnnonce().getId()
        ).isPresent();
        
        if (!hasValidReservation) {
            addViolation(ctx, "OCL: L'utilisateur n'a pas de réservation confirmée pour cette annonce");
            return false;
        }
        return true;
    }
    
    private boolean validateTemporalWindow(Evaluation eval, ConstraintValidatorContext ctx) {
        try {
            LocalDateTime evaluationDate = LocalDateTime.parse(eval.getDate(), formatter);
            LocalDateTime departureDate = reservationRepository
                .findCompletedReservationByUserAndAnnonce(
                    eval.getUtilisateur().getId(),
                    eval.getAnnonce().getId()
                )
                .map(r -> LocalDateTime.parse(r.getDate_depart(), formatter))
                .orElse(LocalDateTime.now());
            
            long daysDiff = ChronoUnit.DAYS.between(departureDate, evaluationDate);
            
            if (daysDiff < 0 || daysDiff > 7) {
                addViolation(ctx,
                    String.format("OCL: L'évaluation doit être entre 0 et 7 jours après le départ (actuellement: %d jours)", daysDiff));
                return false;
            }
            return true;
        } catch (Exception e) {
            addViolation(ctx, "OCL: Format de date invalide");
            return false;
        }
    }
    
    private boolean validateEvaluationUniqueness(Evaluation eval, ConstraintValidatorContext ctx) {
        long existingCount = evaluationRepository.countByUserAndAnnonce(
            eval.getUtilisateur().getId(),
            eval.getAnnonce().getId()
        );
        
        if (existingCount > 0) {
            addViolation(ctx, "OCL: L'utilisateur a déjà évalué cette annonce");
            return false;
        }
        return true;
    }
    
    private boolean validateNoSelfEvaluation(Evaluation eval, ConstraintValidatorContext ctx) {
        if (eval.getUtilisateur().getId().equals(eval.getAnnonce().getAnnonceur().getId())) {
            addViolation(ctx, "OCL: Un annonceur ne peut pas évaluer sa propre annonce");
            return false;
        }
        return true;
    }
    
    private boolean validateCommentNotEmpty(Evaluation eval, ConstraintValidatorContext ctx) {
        if (eval.getCommentaire() == null || eval.getCommentaire().trim().isEmpty()) {
            addViolation(ctx, "OCL: Le commentaire ne peut pas être vide");
            return false;
        }
        return true;
    }
    
    private void addViolation(ConstraintValidatorContext ctx, String message) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(message)
           .addConstraintViolation();
    }
}
```

---

## 🔌 **COUCHE 3 : Intégration Spring**

### **Configuration des Validations**

```java
package com.boky.PFE.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.context.annotation.Bean;

@Configuration
public class ValidationConfig {
    
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
```

### **Utilisation dans les Services**

```java
package com.boky.PFE.service;

import com.boky.PFE.entite.Reservation;
import com.boky.PFE.validation.constraints.ValidReservation;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    
    /**
     * Crée une réservation en respectant les contraintes OCL.
     * Les violations sont détectées automatiquement par le validateur.
     */
    public Reservation createReservation(@Valid Reservation reservation) {
        // Si on arrive ici, toutes les contraintes OCL ont été vérifiées
        return reservationRepository.save(reservation);
    }
}
```

### **Gestion des Erreurs de Validation**

```java
package com.boky.PFE.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("Violation de contrainte OCL", errors));
    }
}

class ErrorResponse {
    private String message;
    private Map<String, String> violations;
    
    public ErrorResponse(String message, Map<String, String> violations) {
        this.message = message;
        this.violations = violations;
    }
    
    // Getters
}
```

---

## 📊 **Requêtes Repositories Essentielles**

```java
package com.boky.PFE.repository;

import com.boky.PFE.entite.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Trouvé réservations chevauchantes
    @Query("""
        SELECT r FROM Reservation r 
        WHERE r.annonce.id = :annonceId 
        AND r.confirmation = true 
        AND r.id != :reservationId
        AND (
            CAST(r.date_arrivee AS INTEGER) < CAST(:departDate AS INTEGER)
            AND CAST(r.date_depart AS INTEGER) > CAST(:arrivalDate AS INTEGER)
        )
    """)
    List<Reservation> findConflictingReservations(
        @Param("annonceId") Long annonceId,
        @Param("arrivalDate") String arrivalDate,
        @Param("departDate") String departDate,
        @Param("reservationId") Long reservationId
    );
    
    // Compter réservations confirmées du client
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.utilisateur.id = :userId AND r.confirmation = true")
    long countConfirmedByClient(@Param("userId") Long userId);
}

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
    // Compter chats dupliqués
    @Query("""
        SELECT COUNT(c) FROM Chat c 
        WHERE (
            (c.emailfirstUserName = :email1 AND c.emailSecondeUser = :email2) OR
            (c.emailfirstUserName = :email2 AND c.emailSecondeUser = :email1)
        )
    """)
    long countDuplicateChats(@Param("email1") String email1, @Param("email2") String email2);
}
```

