package com.boky.PFE.service;

/**
 * Port d’émission de JWT (DIP) : les contrôleurs et services métier ne dépendent pas de jjwt directement.
 */
public interface JwtTokenService {

    /**
     * Crée un JWT avec la claim {@code "data"} contenant l’objet à exposer au client (souvent un utilisateur ou admin).
     */
    String createTokenForUserData(Object data);
}
