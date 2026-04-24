if (evaluation.getCommentaire() == null || evaluation.getCommentaire().trim().isEmpty()) {
    throw new ValidationException(
        " Le commentaire est obligatoire pour publier une évaluation."
    );
}
if (evaluation.getUtilisateur().equals(annonceur)) {
    throw new ValidationException(
        " Vous ne pouvez pas évaluer votre propre annonce."
    );
}
boolean existe = evaluationsAnnonce.stream()
        .anyMatch(e -> e.getUtilisateur().equals(evaluation.getUtilisateur()));

if (existe) {
    throw new ValidationException(
        " Vous avez déjà laissé une évaluation pour cette annonce."
    );
}
boolean reservationValide = reservationsAnnonce.stream()
        .anyMatch(r ->
                r.getUtilisateur().equals(evaluation.getUtilisateur())
                && r.isConfirmation()
                && r.isEtat()
        );

if (!reservationValide) {
    throw new ValidationException(
        " Vous devez avoir effectué un séjour confirmé pour évaluer cette annonce."
    );
}
boolean dansDelai = reservationsAnnonce.stream()
        .filter(r -> r.getUtilisateur().equals(evaluation.getUtilisateur()))
        .anyMatch(r ->
                evaluation.getDateEpochDay() >= r.getDateDepartEpochDay()
                && evaluation.getDateEpochDay() <= r.getDateDepartEpochDay() + 7
        );

if (!dansDelai) {
    throw new ValidationException(
        " Vous ne pouvez évaluer que dans les 7 jours après votre séjour."
    );
}
