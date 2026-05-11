package com.boky.PFE.service;

import com.boky.PFE.entite.Annonce;
import com.boky.PFE.entite.Annonceur;
import com.boky.PFE.entite.Evaluation;
import com.boky.PFE.entite.Reservation;
import com.boky.PFE.entite.Utilisateur;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.OCL;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OclEvaluationValidator {

    private static final List<DateTimeFormatter> DATE_FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    );

    private final OCL ocl;
    private final Constraint evaluationInvariant;

    private final EClass utilisateurClass;
    private final EClass annonceClass;
    private final EClass reservationClass;
    private final EClass evaluationClass;

    private final EAttribute utilisateurIdAttr;
    private final EAttribute reservationConfirmationAttr;
    private final EAttribute reservationEtatAttr;
    private final EAttribute reservationDateDepartEpochDayAttr;
    private final EAttribute evaluationDateEpochDayAttr;
    private final EAttribute evaluationCommentaireAttr;

    private final EReference annonceAnnonceurRef;
    private final EReference annonceReservationsRef;
    private final EReference annonceEvaluationsRef;
    private final EReference reservationUtilisateurRef;
    private final EReference evaluationUtilisateurRef;
    private final EReference evaluationAnnonceRef;

    public OclEvaluationValidator() {
        EcoreFactory factory = EcoreFactory.eINSTANCE;

        EPackage ePackage = factory.createEPackage();
        ePackage.setName("ReservationPlatform");
        ePackage.setNsPrefix("rp");
        ePackage.setNsURI("http://genielogiciels/pfe/reservation-platform");

        utilisateurClass = factory.createEClass();
        utilisateurClass.setName("Utilisateur");
        utilisateurIdAttr = createAttribute(factory, "id", EcorePackage.Literals.ELONG);
        utilisateurClass.getEStructuralFeatures().add(utilisateurIdAttr);

        reservationClass = factory.createEClass();
        reservationClass.setName("Reservation");
        reservationConfirmationAttr = createAttribute(factory, "confirmation", EcorePackage.Literals.EBOOLEAN);
        reservationEtatAttr = createAttribute(factory, "etat", EcorePackage.Literals.EBOOLEAN);
        reservationDateDepartEpochDayAttr = createAttribute(factory, "dateDepartEpochDay", EcorePackage.Literals.EINT);
        reservationUtilisateurRef = createReference(factory, "utilisateur", utilisateurClass, false, false);
        reservationClass.getEStructuralFeatures().add(reservationConfirmationAttr);
        reservationClass.getEStructuralFeatures().add(reservationEtatAttr);
        reservationClass.getEStructuralFeatures().add(reservationDateDepartEpochDayAttr);
        reservationClass.getEStructuralFeatures().add(reservationUtilisateurRef);

        evaluationClass = factory.createEClass();
        evaluationClass.setName("Evaluation");
        evaluationDateEpochDayAttr = createAttribute(factory, "dateEpochDay", EcorePackage.Literals.EINT);
        evaluationCommentaireAttr = createAttribute(factory, "commentaire", EcorePackage.Literals.ESTRING);
        evaluationUtilisateurRef = createReference(factory, "utilisateur", utilisateurClass, false, false);
        evaluationClass.getEStructuralFeatures().add(evaluationDateEpochDayAttr);
        evaluationClass.getEStructuralFeatures().add(evaluationCommentaireAttr);
        evaluationClass.getEStructuralFeatures().add(evaluationUtilisateurRef);

        annonceClass = factory.createEClass();
        annonceClass.setName("Annonce");
        annonceAnnonceurRef = createReference(factory, "annonceur", utilisateurClass, false, false);
        annonceReservationsRef = createReference(factory, "reservations", reservationClass, true, true);
        annonceEvaluationsRef = createReference(factory, "evaluations", evaluationClass, true, true);
        annonceClass.getEStructuralFeatures().add(annonceAnnonceurRef);
        annonceClass.getEStructuralFeatures().add(annonceReservationsRef);
        annonceClass.getEStructuralFeatures().add(annonceEvaluationsRef);

        evaluationAnnonceRef = createReference(factory, "annonce", annonceClass, false, false);
        evaluationClass.getEStructuralFeatures().add(evaluationAnnonceRef);

        ePackage.getEClassifiers().add(utilisateurClass);
        ePackage.getEClassifiers().add(annonceClass);
        ePackage.getEClassifiers().add(reservationClass);
        ePackage.getEClassifiers().add(evaluationClass);

        ocl = OCL.newInstance();
        String invariantExpression = extractInvariantExpression(loadConstraintFile());
        OCL.Helper helper = ocl.createOCLHelper();
        helper.setContext(evaluationClass);
        try {
            evaluationInvariant = helper.createInvariant(invariantExpression);
        } catch (Exception ex) {
            throw new IllegalStateException("Impossible de parser l'invariant OCL pour Evaluation.", ex);
        }
    }

    public void validateBeforeSave(
            Evaluation evaluation,
            Annonce annonce,
            Annonceur annonceur,
            List<Reservation> reservationsAnnonce,
            List<Evaluation> evaluationsAnnonce
    ) {
        
        if (evaluation.getCommentaire() == null
                || evaluation.getCommentaire().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Le commentaire est obligatoire pour publier une évaluation."
            );
        }

        if (evaluation.getUtilisateur().getId()
                .equals(annonceur.getId())) {
            throw new IllegalArgumentException(
                "Vous ne pouvez pas évaluer votre propre annonce."
            );
        }

        boolean dejaEvalue = evaluationsAnnonce.stream()
                .anyMatch(e -> e.getUtilisateur().getId()
                    .equals(evaluation.getUtilisateur().getId()));
        if (dejaEvalue) {
            throw new IllegalArgumentException(
                "Vous avez déjà laissé une évaluation pour cette annonce."
            );
        }

        boolean reservationValide = reservationsAnnonce.stream()
                .anyMatch(r ->
                    r.getUtilisateur().getId()
                        .equals(evaluation.getUtilisateur().getId())
                    && r.isConfirmation()
                    && r.isEtat()
                );
        if (!reservationValide) {
            throw new IllegalArgumentException(
                "Vous devez avoir effectué un séjour confirmé pour évaluer."
            );
        }

        boolean dansDelai = reservationsAnnonce.stream()
                .filter(r -> r.getUtilisateur().getId()
                    .equals(evaluation.getUtilisateur().getId()))
                .anyMatch(r ->
                    toEpochDayInt(evaluation.getDate())
                        >= toEpochDayInt(r.getDate_depart())
                    && toEpochDayInt(evaluation.getDate())
                        <= toEpochDayInt(r.getDate_depart()) + 7
                );
        if (!dansDelai) {
            throw new IllegalArgumentException(
                "Vous ne pouvez évaluer que dans les 7 jours après votre séjour."
            );
        }

       
        Map<Long, EObject> utilisateurs = new HashMap<>();

        EObject annonceObj = new DynamicEObjectImpl(annonceClass);
        annonceObj.eSet(annonceAnnonceurRef, toUtilisateurEObject(annonceur, utilisateurs));

        @SuppressWarnings("unchecked")
        EList<EObject> reservations = (EList<EObject>) annonceObj.eGet(annonceReservationsRef);
        for (Reservation reservation : reservationsAnnonce) {
            reservations.add(toReservationEObject(reservation, utilisateurs));
        }

        @SuppressWarnings("unchecked")
        EList<EObject> evaluations = (EList<EObject>) annonceObj.eGet(annonceEvaluationsRef);
        for (Evaluation existing : evaluationsAnnonce) {
            evaluations.add(toEvaluationEObject(existing, annonceObj, utilisateurs));
        }

        EObject evaluationObj = toEvaluationEObject(evaluation, annonceObj, utilisateurs);
        evaluations.add(evaluationObj);

        OCL.Query query = ocl.createQuery(evaluationInvariant);
        boolean isValid = query.check(evaluationObj);
        if (!isValid) {
            throw new IllegalArgumentException(
                "Violation OCL: l'evaluation ne respecte pas ValiditeComportementale."
            );
        }
    }  

    private EObject toReservationEObject(Reservation reservation, Map<Long, EObject> utilisateurs) {
        EObject reservationObj = new DynamicEObjectImpl(reservationClass);
        reservationObj.eSet(reservationConfirmationAttr, reservation.isConfirmation());
        reservationObj.eSet(reservationEtatAttr, reservation.isEtat());
        reservationObj.eSet(reservationDateDepartEpochDayAttr, toEpochDayInt(reservation.getDate_depart()));
        reservationObj.eSet(reservationUtilisateurRef, toUtilisateurEObject(reservation.getUtilisateur(), utilisateurs));
        return reservationObj;
    }

    private EObject toEvaluationEObject(Evaluation evaluation, EObject annonceObj, Map<Long, EObject> utilisateurs) {
        EObject evaluationObj = new DynamicEObjectImpl(evaluationClass);
        evaluationObj.eSet(evaluationDateEpochDayAttr, toEpochDayInt(evaluation.getDate()));
        evaluationObj.eSet(evaluationCommentaireAttr, evaluation.getCommentaire());
        evaluationObj.eSet(evaluationUtilisateurRef, toUtilisateurEObject(evaluation.getUtilisateur(), utilisateurs));
        evaluationObj.eSet(evaluationAnnonceRef, annonceObj);
        return evaluationObj;
    }

    private EObject toUtilisateurEObject(Utilisateur utilisateur, Map<Long, EObject> utilisateurs) {
        if (utilisateur == null || utilisateur.getId() == null) {
            EObject fallback = new DynamicEObjectImpl(utilisateurClass);
            fallback.eSet(utilisateurIdAttr, -1L);
            return fallback;
        }
        return utilisateurs.computeIfAbsent(utilisateur.getId(), id -> {
            EObject userObj = new DynamicEObjectImpl(utilisateurClass);
            userObj.eSet(utilisateurIdAttr, id);
            return userObj;
        });
    }

    private String loadConstraintFile() {
        ClassPathResource resource = new ClassPathResource("ocl/EvaluationConstraints.ocl");
        try (InputStream input = resource.getInputStream()) {
            byte[] bytes = input.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Impossible de charger ocl/EvaluationConstraints.ocl", ex);
        }
    }

    private String extractInvariantExpression(String content) {
        List<String> lines = Arrays.asList(content.split("\\R"));
        List<String> expressionLines = new ArrayList<>();
        boolean capture = false;
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("inv ") && trimmed.endsWith(":")) {
                capture = true;
                continue;
            }
            if (capture && trimmed.equals("endpackage")) {
                break;
            }
            if (capture) {
                expressionLines.add(line);
            }
        }
        String expression = String.join("\n", expressionLines).trim();
        if (expression.isEmpty()) {
            throw new IllegalStateException("Invariant OCL introuvable dans EvaluationConstraints.ocl");
        }
        return expression;
    }

    private int toEpochDayInt(String rawDate) {
        LocalDate date = parseToLocalDate(rawDate);
        if (date == null) {
            return (int) LocalDate.now().toEpochDay();
        }
        return (int) date.toEpochDay();
    }

    private LocalDate parseToLocalDate(String rawDate) {
        if (rawDate == null || rawDate.trim().isEmpty()) {
            return null;
        }
        String value = rawDate.trim();
        for (DateTimeFormatter formatter : DATE_FORMATS) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
                try {
                    return LocalDateTime.parse(value, formatter).toLocalDate();
                } catch (DateTimeParseException ignoredToo) {
                    // Continue.
                }
            }
        }
        return null;
    }

    private EAttribute createAttribute(EcoreFactory factory, String name, org.eclipse.emf.ecore.EDataType type) {
        EAttribute attr = factory.createEAttribute();
        attr.setName(name);
        attr.setEType(type);
        return attr;
    }

    private EReference createReference(EcoreFactory factory, String name, EClass type, boolean many, boolean containment) {
        EReference ref = factory.createEReference();
        ref.setName(name);
        ref.setEType(type);
        ref.setContainment(containment);
        ref.setUpperBound(many ? -1 : 1);
        return ref;
    }
}