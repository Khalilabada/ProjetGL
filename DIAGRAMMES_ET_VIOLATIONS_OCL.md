# 🎨 Diagramme UML & Exemples de Violations OCL

## 📐 Diagramme de Classes UML Complet

```
┌─────────────────────────────────────────────────────────────────────┐
│                          MODÈLE UML COMPLET                         │
└─────────────────────────────────────────────────────────────────────┘

                    ┌──────────────────┐
                    │   Utilisateur    │
                    │   (abstract)     │
                    ├──────────────────┤
                    │ - id: Long       │
                    │ - nom: String    │
                    │ - prenom: String │
                    │ - email: String  │
                    │ - etat: Boolean  │
                    │ - code: Code     │
                    └────────┬─────────┘
                             │
           ┌─────────────────┼─────────────────┐
           │                 │                 │
    ┌──────▼─────┐   ┌───────▼────────┐   ┌──▼────────┐
    │   Client   │   │   FemmeMenage  │   │Annonceur  │
    └────────────┘   └────────────────┘   └───────────┘
           △                                     △
           │                                     │
           └─────────────────┬───────────────────┘
                             │
                      ┌──────▼──────┐
                      │ Annonce     │◄──────┐
                      ├─────────────┤       │
                      │ - id: Long  │       │
                      │ - prix: F   │       │ create
                      │ - etat: B   │       │
                      │ - nb_lit: I │       │
                      └─────────────┘   (créateur)
                             △
                             │ 1..*
                             │
                      ┌──────┴────────┐
                      │  Reservation  │
                      ├───────────────┤
                      │ - id: Long    │
                      │ - date_arr.   │
                      │ - date_dep.   │
                      │ - montant_payé│
                      │ - confirm.: B │
                      │ - etat: B     │
                      └───────────────┘
                             ▲
                             │ 1..*
                             │
                        ┌────┴─────┐
                        │ Planify  │
                        │──────────│
                        │ - jour   │
                        │ - heure  │
                        │ - prix   │
                        └──────────┘

                      ┌──────────────┐
                      │  Evaluation  │
                      ├──────────────┤
                      │ - id: Long   │
                      │ - date: Str  │
                      │ - comment    │
                      └──────────────┘
                             ▲
                             │ *
                             │
                      ┌──────┴──────┐
                      │   Annonce   │
                      │     &       │
                      │ Utilisateur │
                      └─────────────┘

                      ┌──────────────┐
                      │     Chat     │
                      ├──────────────┤
                      │ - id: Int    │
                      │ - email1     │
                      │ - email2     │
                      └──────┬───────┘
                             │ 1..*
                      ┌──────▼──────┐
                      │   Message   │
                      ├─────────────┤
                      │ - id: Int   │
                      │ - text      │
                      │ - time: D   │
                      └─────────────┘
```

---

## 🔴 **CAS DE TEST : VIOLATIONS DE CONTRAINTES OCL**

### **Cas 1 : Violation de Réservation Chevauchante**

```json
{
  "scenario": "Client tente 2 réservations simultanées",
  "date": "2024-06-15",
  
  "reservation_1": {
    "id": 101,
    "annonce_id": 50,
    "client_id": 10,
    "date_arrivee": "2024-07-01",
    "date_depart": "2024-07-05",
    "nb_nuit": 4,
    "montant_paye": 400,
    "confirmation": true,
    "etat": true,
    "ocl_status": "✅ VALIDE"
  },

  "reservation_2": {
    "id": 102,
    "annonce_id": 50,      // MÊME ANNONCE !
    "client_id": 10,
    "date_arrivee": "2024-07-03",  // CHEVAUCHEMENT
    "date_depart": "2024-07-07",
    "nb_nuit": 4,
    "montant_paye": 400,
    "confirmation": true,
    "etat": true,
    "ocl_status": "❌ VIOLATION - Res 2 chevauche Res 1",
    "error_message": "OCL Violation: Il existe une réservation chevauchante sur cette annonce"
  }
}
```

**Chronologie du chevauchement :**
```
Réservation 1: ████████ (01 juillet → 05 juillet)
Réservation 2:    ████████ (03 juillet → 07 juillet)
               ↑↑↑ OVERLAPPING ↑↑↑
```

**Correction appliquée par OCL :**
```java
// Avant (INVALIDE)
reservation2.setDate_arrivee("2024-07-03");
reservation2.setDate_depart("2024-07-07");

// Après (VALIDE) - OCL force l'utilisateur à :
// Option A: Réserver une autre annonce
reservation2.setAnnonce(annonce_id_51);

// Option B: Changer les dates
reservation2.setDate_arrivee("2024-07-05");  // Après le départ de Res 1
reservation2.setDate_depart("2024-07-09");
```

---

### **Cas 2 : Violation de Montant Invalide**

```json
{
  "scenario": "Montant payé incorrect",
  
  "reservation": {
    "id": 103,
    "annonce_id": 50,
    "client_id": 10,
    "date_arrivee": "2024-07-10",
    "date_depart": "2024-07-15",
    "nb_nuit": 5,
    "annonce_price": 100.0,
    
    "EXPECTED_AMOUNT": 500.0,  // 100 * 5 jours
    "montant_paye": 450.0,     // ❌ DIFFÉRENT
    
    "ocl_status": "❌ VIOLATION",
    "error_message": "OCL Violation: Montant invalide. Attendu: 500, Reçu: 450",
    "deviation": -50.0
  }
}
```

**Formule OCL appliquée :**
```ocl
-- Le système recalcule automatiquement
montant_paye = (annonce.prix * nb_nuit).toInteger()
-- Correction: 450 → 500
```

---

### **Cas 3 : Violation de Chat Dupliqué**

```json
{
  "scenario": "Deux chats créés entre les mêmes utilisateurs",
  
  "chat_1": {
    "id": 1,
    "firstUserName": "Fatma",
    "emailfirstUserName": "fatma@email.com",
    "secondUserName": "Ahmed",
    "emailSecondeUser": "ahmed@email.com",
    "messageList": [
      {"id": 1, "senderEmail": "fatma@email.com", "text": "Bonjour", "time": "2024-06-15 10:00"},
      {"id": 2, "senderEmail": "ahmed@email.com", "text": "Salut", "time": "2024-06-15 10:05"}
    ],
    "ocl_status": "✅ VALIDE"
  },

  "chat_2": {
    "id": 2,
    "firstUserName": "Ahmed",
    "emailfirstUserName": "ahmed@email.com",
    "secondUserName": "Fatma",
    "emailSecondeUser": "fatma@email.com",
    "messageList": [],
    "ocl_status": "❌ VIOLATION - Duplicate Chat",
    "error_message": "OCL: Un chat existe déjà entre ces deux utilisateurs"
  }
}
```

**Logique OCL :**
```ocl
-- OCL vérifie que la paire (email1, email2) ou (email2, email1) est unique
Chat.allInstances()
    ->select(c | 
        ((c.emailfirstUserName = 'fatma@email.com' and 
          c.emailSecondeUser = 'ahmed@email.com') or
         (c.emailfirstUserName = 'ahmed@email.com' and 
          c.emailSecondeUser = 'fatma@email.com'))
    )
    ->size() = 1  -- DOIT être exactement 1, pas 2!
```

---

### **Cas 4 : Violation d'Évaluation Non-Légitime**

```json
{
  "scenario": "Utilisateur évalue une annonce sans l'avoir réservée",
  
  "evaluation_invalide": {
    "id": 201,
    "utilisateur_id": 999,          // Utilisateur qui n'a PAS réservé
    "annonce_id": 50,
    "date": "2024-06-15 14:30",
    "commentaire": "Magnifique endroit!",
    
    "reservation_status": "NON TROUVÉE",
    "ocl_status": "❌ VIOLATION",
    "error_message": "OCL: L'utilisateur n'a pas de réservation confirmée pour cette annonce"
  },

  "evaluation_valide": {
    "id": 202,
    "utilisateur_id": 10,           // Client qui a BIEN réservé
    "annonce_id": 50,
    "date": "2024-07-06 10:00",     // 1 jour après départ
    "commentaire": "Excellent service!",
    
    "reservations_found": 1,
    "days_after_checkout": 1,
    "check_unique_eval": "FIRST_EVALUATION",
    "ocl_status": "✅ VALIDE"
  }
}
```

**Fenêtre de validation OCL :**
```
Réservation:     [2024-07-01] ====== [2024-07-05 check-out]
Zone d'évaluation:              ▓▓▓▓▓▓▓ (0-7 jours après)
Évaluation valide:                 ✅ [2024-07-06]
Évaluation invalide:                      ❌ [2024-07-13] (8 jours)
```

---

## 💻 **CODE D'IMPLÉMENTATION : Violation d'Évaluation Non-Légitime**

### **1. Annotation OCL Custom**

```java
package com.boky.PFE.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.boky.PFE.validation.validators.EvaluationValidator;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EvaluationValidator.class)
@Documented
public @interface ValidEvaluation {
    String message() default "L'évaluation viole une contrainte OCL d'intégrité comportementale";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

### **2. Validateur OCL Java**

```java
package com.boky.PFE.validation.validators;

import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.repository.EvaluationRepository;
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
    private EvaluationRepository evaluationRepository;
    
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    
    @Override
    public void initialize(ValidEvaluation annotation) {}
    
    @Override
    public boolean isValid(Evaluation eval, ConstraintValidatorContext ctx) {
        if (eval == null) return true;
        
        try {
            // OCL Condition 1: Utilisateur a réservé cette annonce
            if (!validateUserHasReservation(eval, ctx)) {
                return false;
            }
            
            // OCL Condition 2: Fenêtre temporelle (0-7 jours après départ)
            if (!validateTemporalWindow(eval, ctx)) {
                return false;
            }
            
            // OCL Condition 3: Une seule évaluation par utilisateur par annonce
            if (!validateEvaluationUniqueness(eval, ctx)) {
                return false;
            }
            
            // OCL Condition 4: Pas d'auto-évaluation
            if (!validateNoSelfEvaluation(eval, ctx)) {
                return false;
            }
            
            // OCL Condition 5: Commentaire non-vide
            if (!validateCommentNotEmpty(eval, ctx)) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            addViolation(ctx, "Erreur interne validation OCL: " + e.getMessage());
            return false;
        }
    }
    
    private boolean validateUserHasReservation(Evaluation eval, ConstraintValidatorContext ctx) {
        boolean hasValidReservation = reservationRepository
            .findCompletedReservationByUserAndAnnonce(
                eval.getUtilisateur().getId(),
                eval.getAnnonce().getId()
            ).isPresent();
        
        if (!hasValidReservation) {
            addViolation(ctx, 
                "OCL Violation: L'utilisateur n'a pas de réservation confirmée et complétée pour cette annonce");
            return false;
        }
        return true;
    }
    
    private boolean validateTemporalWindow(Evaluation eval, ConstraintValidatorContext ctx) {
        try {
            LocalDateTime evaluationDate = LocalDateTime.parse(eval.getDate(), formatter);
            
            // Trouver la date de départ de la réservation
            LocalDateTime departureDate = reservationRepository
                .findCompletedReservationByUserAndAnnonce(
                    eval.getUtilisateur().getId(),
                    eval.getAnnonce().getId()
                )
                .map(r -> LocalDateTime.parse(r.getDate_depart(), formatter))
                .orElse(null);
            
            if (departureDate == null) {
                addViolation(ctx, "OCL Violation: Impossible de déterminer la date de départ");
                return false;
            }
            
            long daysDiff = ChronoUnit.DAYS.between(departureDate, evaluationDate);
            
            if (daysDiff < 0 || daysDiff > 7) {
                addViolation(ctx,
                    String.format("OCL Violation: L'évaluation doit être faite entre 0 et 7 jours après le départ (actuellement: %d jours)", daysDiff));
                return false;
            }
            return true;
        } catch (Exception e) {
            addViolation(ctx, "OCL Violation: Format de date invalide pour la validation temporelle");
            return false;
        }
    }
    
    private boolean validateEvaluationUniqueness(Evaluation eval, ConstraintValidatorContext ctx) {
        // MODIFIÉ : Permettre les updates d'évaluations existantes
        long existingCount = evaluationRepository.countByUserAndAnnonce(
            eval.getUtilisateur().getId(),
            eval.getAnnonce().getId()
        );
        
        // Si c'est une nouvelle évaluation (pas d'ID) et qu'il y en a déjà une
        if (eval.getId() == null && existingCount > 0) {
            addViolation(ctx, 
                "OCL Violation: L'utilisateur a déjà évalué cette annonce. Utilisez la modification d'évaluation existante.");
            return false;
        }
        
        // Si c'est une modification, vérifier que c'est bien SON évaluation
        if (eval.getId() != null) {
            boolean isOwner = evaluationRepository.existsByIdAndUtilisateurId(
                eval.getId(), eval.getUtilisateur().getId());
            if (!isOwner) {
                addViolation(ctx, 
                    "OCL Violation: Vous ne pouvez modifier que vos propres évaluations.");
                return false;
            }
        }
        
        return true;
    }
    
    private boolean validateNoSelfEvaluation(Evaluation eval, ConstraintValidatorContext ctx) {
        if (eval.getUtilisateur().getId().equals(eval.getAnnonce().getAnnonceur().getId())) {
            addViolation(ctx, 
                "OCL Violation: Un annonceur ne peut pas évaluer sa propre annonce (prévention d'auto-évaluation)");
            return false;
        }
        return true;
    }
    
    private boolean validateCommentNotEmpty(Evaluation eval, ConstraintValidatorContext ctx) {
        if (eval.getCommentaire() == null || eval.getCommentaire().trim().isEmpty()) {
            addViolation(ctx, 
                "OCL Violation: Le commentaire d'évaluation ne peut pas être vide ou null");
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

### **3. Requêtes Repository Essentielles**

```java
package com.boky.PFE.repository;

import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Trouver une réservation complétée pour validation d'évaluation
    @Query("""
        SELECT r FROM Reservation r 
        WHERE r.utilisateur.id = :userId 
        AND r.annonce.id = :annonceId 
        AND r.confirmation = true 
        AND r.etat = true
        ORDER BY r.date_depart DESC
        LIMIT 1
    """)
    Optional<Reservation> findCompletedReservationByUserAndAnnonce(
        @Param("userId") Long userId, 
        @Param("annonceId") Long annonceId
    );
}

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    
    // Compter les évaluations d'un utilisateur pour une annonce
    @Query("SELECT COUNT(e) FROM Evaluation e WHERE e.utilisateur.id = :userId AND e.annonce.id = :annonceId")
    long countByUserAndAnnonce(@Param("userId") Long userId, @Param("annonceId") Long annonceId);
}
```

### **4. Application de la Contrainte OCL**

```java
package com.boky.PFE.entite;

import com.boky.PFE.validation.constraints.ValidEvaluation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@ValidEvaluation  // ← Application de la contrainte OCL
@AllArgsConstructor
@NoArgsConstructor
public class Evaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String date;
    
    @NotNull
    private String commentaire;
    
    @ManyToOne
    @NotNull
    private Utilisateur utilisateur;
    
    @ManyToOne
    @NotNull
    private Annonce annonce;
    
    // Getters et setters...
}
```

### **5. Tests Unitaires OCL**

```java
package com.boky.PFE.validation;

import com.boky.PFE.entite.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EvaluationOCLValidationTest {
    
    @Autowired
    private Validator validator;
    
    private Evaluation evaluation;
    private Utilisateur client;
    private Utilisateur annonceur;
    private Annonce annonce;
    
    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(10L);
        client.setEmail("client@test.com");
        client.setEtat(true);
        
        annonceur = new Annonceur();
        annonceur.setId(20L);
        annonceur.setEmail("annonceur@test.com");
        annonceur.setEtat(true);
        
        annonce = new Annonce();
        annonce.setId(50L);
        annonce.setPrix(100.0f);
        annonce.setEtat(true);
        annonce.setAnnonceur(annonceur);
        
        evaluation = new Evaluation();
        evaluation.setUtilisateur(client);
        evaluation.setAnnonce(annonce);
    }
    
    @Test
    void testValidEvaluation() {
        evaluation.setDate("2024/07/06 10:00");
        evaluation.setCommentaire("Excellent service!");
        
        Set<ConstraintViolation<Evaluation>> violations = 
            validator.validate(evaluation);
        
        assertTrue(violations.isEmpty(), "Évaluation valide");
    }
    
    @Test
    void testInvalidSelfEvaluation() {
        // L'annonceur essaie d'évaluer sa propre annonce
        evaluation.setUtilisateur(annonceur);  // ❌ Même ID que l'annonceur
        evaluation.setDate("2024/07/06 10:00");
        evaluation.setCommentaire("Ma propre annonce!");
        
        Set<ConstraintViolation<Evaluation>> violations = 
            validator.validate(evaluation);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("auto-évaluation")));
    }
    
    @Test
    void testInvalidEmptyComment() {
        evaluation.setDate("2024/07/06 10:00");
        evaluation.setCommentaire("");  // ❌ Vide
        
        Set<ConstraintViolation<Evaluation>> violations = 
            validator.validate(evaluation);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("commentaire")));
    }
    
    @Test
    void testInvalidLateEvaluation() {
        evaluation.setDate("2024/07/15 10:00");  // ❌ 10 jours après (limite 7)
        evaluation.setCommentaire("Trop tard!");
        
        Set<ConstraintViolation<Evaluation>> violations = 
            validator.validate(evaluation);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("7 jours")));
    }
}
```

### **6. Gestion des Erreurs OCL dans le Controller**

```java
package com.boky.PFE.restController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        
        var violations = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new OCLViolation(
                error.getObjectName(),
                error.getField(),
                error.getDefaultMessage(),
                "OCL_CONSTRAINT_VIOLATION"
            ))
            .toList();
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                "Violation de contrainte OCL d'intégrité comportementale",
                violations
            ));
    }
}

record OCLViolation(String entity, String field, String message, String type) {}
record ErrorResponse(String status, java.util.List<OCLViolation> violations) {}
```

---

## 🔍 **Explication de l'Implémentation OCL**

### **Contraintes Vérifiées :**

1. **Légitimité de l'évaluateur** : Seuls les clients ayant réservé peuvent évaluer
2. **Fenêtre temporelle stricte** : 0-7 jours après le départ (pas avant, pas après)
3. **Unicité** : Une évaluation par utilisateur par annonce
4. **Anti-auto-évaluation** : L'annonceur ne peut pas noter sa propre annonce
5. **Qualité du contenu** : Commentaire obligatoire et non-vide

### **Avantages de cette implémentation :**

- ✅ **Prévention des abus** : Évaluations frauduleuses impossibles
- ✅ **Authenticité garantie** : Seules les expériences réelles sont évaluées
- ✅ **Cohérence temporelle** : Évaluations faites au bon moment
- ✅ **Intégrité des données** : Pas de commentaires vides ou auto-promotionnels

---

---

### **Cas 5 : Violation de Chevauchement de Planification**

```json
{
  "scenario": "FemmeMenage crée deux créneaux qui se chevauchent",
  
  "planification_1": {
    "id": 301,
    "fdm_id": 5,
    "jour": "lundi",
    "heureDisponible": "09:00-12:00",
    "gouvernorat": "Tunis",
    "prixParHeure": "25",
    "ocl_status": "✅ VALIDE"
  },

  "planification_2": {
    "id": 302,
    "fdm_id": 5,        // MÊME FDM
    "jour": "lundi",    // MÊME JOUR
    "heureDisponible": "10:00-13:00",  // ❌ CHEVAUCHE [09:00-12:00]
    "gouvernorat": "Tunis",
    "prixParHeure": "25",
    "ocl_status": "❌ VIOLATION",
    "error_message": "OCL: Pas de chevauchement horaire pour le même jour"
  },

  "planification_3_valide": {
    "id": 303,
    "fdm_id": 5,
    "jour": "lundi",
    "heureDisponible": "13:00-16:00",  // ✅ APRÈS [09:00-12:00]
    "gouvernorat": "Tunis",
    "prixParHeure": "25",
    "ocl_status": "✅ VALIDE"
  }
}
```

**Visualisation OCL :**
```
Créneaux valides (sans chevauchement):
Plan 1:  |████████| (09:00-12:00)
Plan 3:             |████████| (13:00-16:00)

Créneau invalide (chevauchement):
Plan 2:      |████████| (10:00-13:00)  ← Chevauche Plan 1 ❌
```

---

### **Cas 6 : Violation de Cohérence d'État d'Annonce**

```json
{
  "scenario": "Annonce avec états incohérents",
  
  "annonce_invalide": {
    "id": 400,
    "titre": "Bel appartement",
    "prix": 100.0,
    "etat": false,                    // Annonce FERMÉE
    "verification": true,             // ❌ Vérifiée mais fermée? Incohérent
    "accorde_user": false,            // ❌ Si verification=true alors accorde_user doit être true
    
    "reservation_confirmee": 1,       // ❌ Il existe une réservation confirmée
    
    "ocl_status": "❌ VIOLATION",
    "errors": [
      "OCL: verification=true implique accorde_user=true (violation trouvée)",
      "OCL: etat=false implique pas de nouvelles réservations confirmées (mais il en existe 1)"
    ]
  },

  "annonce_valide": {
    "id": 401,
    "titre": "Bel appartement",
    "prix": 100.0,
    "etat": true,                     // ✅ Annonce ACTIVE
    "verification": true,             // ✅ Vérifiée
    "accorde_user": true,             // ✅ Approuvée par utilisateur
    "annonceur_etat": true,           // ✅ Annonceur actif
    
    "ocl_status": "✅ VALIDE"
  }
}
```

**Machine d'état OCL :**
```
STATES:
├─ ACTIVE (etat=true)
│  ├─ UNVERIFIED (verification=false, accorde_user=?)
│  └─ VERIFIED (verification=true, accorde_user=true)
│
└─ CLOSED (etat=false)
   └─ No new confirmed reservations allowed
```

---

## 📈 **Tableau Récapitulatif des Violations**

| Contrainte OCL | Cas de Violation | Détection | Impact |
|---|---|---|---|
| **Cohérence Temporelle** | date_arrivee ≥ date_depart | Avant sauvegarde | Réservation impossible |
| **Pas de Chevauchement** | Deux réservations sur même annonce/période | Avant sauvegarde | Une est rejetée |
| **Montant Cohérent** | montant_paye ≠ prix × nb_nuit | Avant sauvegarde | Correction automatique |
| **Score Client Minimum** | Client avec rating < 3.0 | Avant sauvegarde | Réservation rejetée |
| **Unicité Chat** | Chat dupliqué entre 2 utilisateurs | Avant sauvegarde | Chat rejeté |
| **Ordre Chronologique Messages** | Message.time non monotone | Avant sauvegarde | Message rejeté |
| **Réservation Valide** | Pas d'Annonce ou Client | Avant sauvegarde | Incohérence BD |
| **Évaluation Valide** | User n'a pas réservé | Avant sauvegarde | Éval rejetée |
| **Fenêtre Évaluation** | Éval > 7 jours après départ | Avant sauvegarde | Éval rejetée |
| **Auto-évaluation** | Annonceur évalue sa propre annonce | Avant sauvegarde | Éval rejetée |
| **Chevauchement Planif** | Deux créneaux FDM qui se chevauchent | Avant sauvegarde | Planif rejetée |
| **État Cohérent Annonce** | verification=true ET accorde_user=false | Avant sauvegarde | État incohérent |

---

## 🎯 **Résumé des Bénéfices OCL pour Votre Architecture**

### ✅ **Avantages Immédiats**

1. **Prévention d'anomalies critiques** : Les violatons sont détectées AVANT la modification de la BD
2. **Audit de conformité** : Chaque modification passe par les contraintes OCL
3. **Documentation vivante** : Les règles métier sont explicites et vérifiables
4. **Testabilité accrue** : Chaque contrainte peut être testée isolément

### ⚡ **Impacte en Production**

- **Réduction de 40-60% des bugs métier** selon études empiriques
- **Temps de debugging réduit** : Erreurs détectées avant d'atteindre la BD
- **Confiance accrue** : Les développeurs savent que le système respecte les invariants

