# 🚀 GUIDE COMPLET : De OCL à Implémentation Production

## Phase 1️⃣ : Choix et Configuration de l'Outil OCL

### Option 1: **USE (UML System Environment)** - Recommandé pour débuter

```bash
# Télécharger depuis : https://sourceforge.net/projects/useocl/

# Installation
cd use_4.3.0_linux64
./bin/use

# Créer un fichier model.use
```

**Exemple de modèle USE compatible :**

```use
model Reservation_System

class Utilisateur
  attributes
    id : Integer
    nom : String
    email : String
    etat : Boolean
end

class Reservation
  attributes
    id : Integer
    date_arrivee : String
    date_depart : String
    nb_nuit : Integer
    montant_paye : Real
    confirmation : Boolean
end

association HasReservation between
  Utilisateur[1] role client
  Reservation[*] role reservations
end

-- Contrainte OCL
context Reservation
inv TemporalCoherence:
  self.date_arrivee < self.date_depart

inv MoneyIsCorrect:
  self.confirmation = true implies
    self.montant_paye > 0
```

---

### Option 2: **Eclipse UML Modeling Tools**

```bash
# Installation via Eclipse IDE
Help → Install New Software
→ http://download.eclipse.org/modeling/mdt/uml2/updates/5.5
```

**Configuration Eclipse :**
1. New → UML Project
2. Create new model file (`.uml`)
3. Dans Properties view : Add constraints (OCL)

---

### Option 3: **Papyrus** - Pour une approche complète

```bash
# Installation
Eclipse → Help → Install New Software
→ http://download.eclipse.org/modeling/mdt/papyrus/updates/

# Créer une nouvelle Papyrus model
File → New → Papyrus Model
```

---

## Phase 2️⃣ : Implémentation Spring Boot Intégrée

### Étape 1: Ajouter les dépendances Maven

```xml
<!-- pom.xml -->
<dependencies>
    <!-- Jakarta Bean Validation API -->
    <dependency>
        <groupId>jakarta.validation</groupId>
        <artifactId>jakarta.validation-api</artifactId>
        <version>3.0.2</version>
    </dependency>

    <!-- Hibernate Validator (implémentation) -->
    <dependency>
        <groupId>org.hibernate.validator</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>8.0.0.Final</version>
    </dependency>

    <!-- Spring Boot Validation Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Utile pour les contraintes complexes -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
</dependencies>
```

---

### Étape 2: Structure du Projet

```
src/main/java/com/boky/PFE/
├── validation/
│   ├── constraints/
│   │   ├── ValidReservation.java
│   │   ├── ValidChat.java
│   │   ├── ValidEvaluation.java
│   │   ├── ValidPlanification.java
│   │   └── ValidAnnonce.java
│   ├── validators/
│   │   ├── ReservationValidator.java
│   │   ├── ChatValidator.java
│   │   ├── EvaluationValidator.java
│   │   ├── PlanificationValidator.java
│   │   └── AnnonceValidator.java
│   └── config/
│       └── ValidationConfig.java
├── entite/
│   ├── Reservation.java (annoté @ValidReservation)
│   ├── Chat.java (annoté @ValidChat)
│   ├── Evaluation.java (annoté @ValidEvaluation)
│   └── ...
├── repository/
│   └── (requêtes JPQL correspondantes)
└── restController/
    └── (gestion des erreurs de validation)
```

---

### Étape 3: Implémentation des Validateurs (Fichiers Prêts à Copier)

**A. Créer ReservationValidator.java complète**

```java
package com.boky.PFE.validation.validators;

import com.boky.PFE.entite.Reservation;
import com.boky.PFE.repository.ReservationRepository;
import com.boky.PFE.validation.constraints.ValidReservation;
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
    
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy/MM/dd");
    
    @Override
    public void initialize(ValidReservation annotation) {}
    
    @Override
    public boolean isValid(Reservation res, ConstraintValidatorContext ctx) {
        if (res == null) return true;
        
        // Si pas confirmée, on skip la validation complète
        if (!res.isConfirmation()) return true;
        
        try {
            // 1. Validation temporelle
            if (!checkTemporalValidity(res, ctx)) return false;
            
            // 2. Validation pas de chevauchement
            if (!checkNoOverlap(res, ctx)) return false;
            
            // 3. Validation montant
            if (!checkAmount(res, ctx)) return false;
            
            // 4. Validation références
            if (!checkReferences(res, ctx)) return false;
            
            return true;
        } catch (Exception e) {
            addViolation(ctx, "Erreur interne validation: " + e.getMessage());
            return false;
        }
    }
    
    private boolean checkTemporalValidity(Reservation res, ConstraintValidatorContext ctx) {
        try {
            LocalDate arrival = LocalDate.parse(res.getDate_arrivee(), formatter);
            LocalDate departure = LocalDate.parse(res.getDate_depart(), formatter);
            
            if (arrival.isAfter(departure) || arrival.isEqual(departure)) {
                addViolation(ctx, 
                    "OCL: date_arrivee doit être avant date_depart");
                return false;
            }
            return true;
        } catch (Exception e) {
            addViolation(ctx, "Format date invalide");
            return false;
        }
    }
    
    private boolean checkNoOverlap(Reservation res, ConstraintValidatorContext ctx) {
        // Seulement si l'annonce existe
        if (res.getAnnonce() == null || res.getAnnonce().getId() == null) {
            return true;
        }
        
        List<Reservation> conflicts = reservationRepository
            .findOverlappingReservations(
                res.getAnnonce().getId(),
                res.getDate_arrivee(),
                res.getDate_depart(),
                res.getId() != null ? res.getId() : -1L
            );
        
        if (!conflicts.isEmpty()) {
            addViolation(ctx,
                "OCL: Réservation chevauchante existe déjà pour ces dates");
            return false;
        }
        return true;
    }
    
    private boolean checkAmount(Reservation res, ConstraintValidatorContext ctx) {
        if (res.getAnnonce() == null) return true;
        
        double expectedAmount = res.getAnnonce().getPrix() * res.getNb_nuit();
        
        if (Math.abs(res.getMontant_paye() - expectedAmount) > 0.01) {
            addViolation(ctx,
                String.format("OCL: Montant invalide. Attendu: %.2f, Reçu: %.2f",
                    expectedAmount, res.getMontant_paye()));
            return false;
        }
        return true;
    }
    
    private boolean checkReferences(Reservation res, ConstraintValidatorContext ctx) {
        if (res.getUtilisateur() == null) {
            addViolation(ctx, "OCL: Client manquant");
            return false;
        }
        if (res.getAnnonce() == null) {
            addViolation(ctx, "OCL: Annonce manquante");
            return false;
        }
        if (!res.getUtilisateur().isEtat()) {
            addViolation(ctx, "OCL: Client inactif");
            return false;
        }
        if (!res.getAnnonce().isEtat()) {
            addViolation(ctx, "OCL: Annonce inactive");
            return false;
        }
        return true;
    }
    
    private void addViolation(ConstraintValidatorContext ctx, String msg) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(msg)
           .addConstraintViolation();
    }
}
```

---

### Étape 4: Ajouter les Requêtes Repository

```java
// ReservationRepository.java
package com.boky.PFE.repository;

import com.boky.PFE.entite.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    @Query("""
        SELECT r FROM Reservation r 
        WHERE r.annonce.id = :annonceId 
        AND r.confirmation = true 
        AND r.id != :reservationId
        AND NOT (
            CAST(r.date_depart AS DATE) <= CAST(:arrival AS DATE)
            OR CAST(r.date_arrivee AS DATE) >= CAST(:departure AS DATE)
        )
    """)
    List<Reservation> findOverlappingReservations(
        @Param("annonceId") Long annonceId,
        @Param("arrival") String arrivalDate,
        @Param("departure") String departureDate,
        @Param("reservationId") Long reservationId
    );
}
```

---

### Étape 5: Utiliser la Validation dans les Entités

```java
package com.boky.PFE.entite;

import com.boky.PFE.validation.constraints.ValidReservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@ValidReservation  // ← Applique la contrainte OCL
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String date_arrivee;
    
    @NotNull
    private String date_depart;
    
    private long nb_nuit;
    private long montant_paye;
    private boolean confirmation;
    private boolean etat;
    
    // ... getters/setters
}
```

---

### Étape 6: Gestion des Erreurs dans le Controller

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
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException ex) {
        
        BindingResult result = ex.getBindingResult();
        
        // Extraire les violations OCL
        var violations = result.getFieldErrors().stream()
            .map(e -> new ErrorDetail(
                e.getField(),
                e.getDefaultMessage(),
                "OCL_CONSTRAINT_VIOLATION"
            ))
            .toList();
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                "Validation échouée",
                violations
            ));
    }
}

record ErrorDetail(String field, String message, String code) {}

record ErrorResponse(String status, java.util.List<ErrorDetail> errors) {}
```

---

## Phase 3️⃣ : Tests des Contraintes OCL

### Fichier de Tests JUnit 5

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
public class ReservationOCLValidationTest {
    
    @Autowired
    private Validator validator;
    
    private Reservation reservation;
    private Annonce annonce;
    private Client client;
    
    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setEmail("client@test.com");
        client.setEtat(true);
        
        annonce = new Annonce();
        annonce.setId(1L);
        annonce.setPrix(100.0f);
        annonce.setEtat(true);
        
        reservation = new Reservation();
        reservation.setAnnonce(annonce);
        reservation.setUtilisateur(client);
    }
    
    @Test
    void testValidReservation() {
        // Valide
        reservation.setDate_arrivee("2024-07-01");
        reservation.setDate_depart("2024-07-05");
        reservation.setNb_nuit(4);
        reservation.setMontant_paye(400);
        reservation.setConfirmation(true);
        
        Set<ConstraintViolation<Reservation>> violations = 
            validator.validate(reservation);
        
        assertTrue(violations.isEmpty(), "Réservation valide");
    }
    
    @Test
    void testInvalidTemporalCoherence() {
        // Dates invalides
        reservation.setDate_arrivee("2024-07-05");
        reservation.setDate_depart("2024-07-01");  // APRÈS arrivée!
        reservation.setNb_nuit(4);
        reservation.setMontant_paye(400);
        reservation.setConfirmation(true);
        
        Set<ConstraintViolation<Reservation>> violations = 
            validator.validate(reservation);
        
        assertFalse(violations.isEmpty(), "Doit avoir une violation");
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("date_arrivee")));
    }
    
    @Test
    void testInvalidAmount() {
        // Montant incorrect
        reservation.setDate_arrivee("2024-07-01");
        reservation.setDate_depart("2024-07-05");
        reservation.setNb_nuit(4);
        reservation.setMontant_paye(350);  // Attendu: 400
        reservation.setConfirmation(true);
        
        Set<ConstraintViolation<Reservation>> violations = 
            validator.validate(reservation);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Montant")));
    }
}
```

---

## Phase 4️⃣ : Vérification en Production

### Configuration Logging

```yaml
# application.properties
# Activer logs de validation
logging.level.org.hibernate.validator=DEBUG
logging.level.com.boky.PFE.validation=DEBUG

# Logs détaillés des violations
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Monitoring des Violations

```java
@Aspect
@Component
@Slf4j
public class ValidationAspect {
    
    @AfterThrowing(
        pointcut = "execution(* com.boky.PFE.service.*Service.*(..))",
        throwing = "ex"
    )
    public void logValidationError(JoinPoint jp, Exception ex) {
        if (ex instanceof ConstraintViolationException cve) {
            cve.getConstraintViolations().forEach(v ->
                log.error("OCL VIOLATION: {} - {}",
                    v.getPropertyPath(),
                    v.getMessage())
            );
        }
    }
}
```

---

## 🎓 **Checklist Finale**

### ✅ Avant de déployer en production

- [ ] Tous les validateurs testés unitairement
- [ ] Tests d'intégration passants
- [ ] Documentation OCL à jour
- [ ] Logs de validation configurés
- [ ] Équipe aware des contraintes
- [ ] Monitoring des violations activé
- [ ] Plan de rollback au cas où

### 📊 **Métriques de Succès**

| Métrique | Baseline | Target (1 mois) |
|----------|----------|-----------------|
| Bugs métier en prod | N/A | -40% |
| Temps de debug | N/A | -50% |
| Violations détectées | 0 | >10 par jour |
| Test coverage | N/A | >95% |

