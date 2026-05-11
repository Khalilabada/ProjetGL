package com.boky.PFE.service;

import com.boky.PFE.Beans.UtilisateurRequest;
import org.springframework.http.ResponseEntity;

public interface UtilisateurApplicationService {
    ResponseEntity<?> register(UtilisateurRequest request);
}
