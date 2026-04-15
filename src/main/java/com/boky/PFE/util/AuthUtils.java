package com.boky.PFE.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public class AuthUtils {

    private static final String SECRET_KEY = "SECRET";


    public static Long getCurrentUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();


            if (token.startsWith("\"") && token.endsWith("\"")) {
                token = token.substring(1, token.length() - 1);
            }

            System.out.println("DEBUG JWT - Token reçu : " + token);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();

                Map<String, Object> data = (Map<String, Object>) claims.get("data");
                System.out.println("DEBUG JWT - Claim 'data' : " + data);

                if (data != null && data.get("id") != null) {
                    Long id = Long.valueOf(data.get("id").toString());
                    System.out.println("DEBUG JWT - ID Extrait : " + id);
                    return id;
                }
            } catch (Exception e) {
                System.out.println("Erreur de decodage de token : " + e.getMessage());
                return null;
            }
        }
        return null;
    }
}
