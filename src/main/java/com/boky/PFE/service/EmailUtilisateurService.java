package com.boky.PFE.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailUtilisateurService {
    void sendEmail(SimpleMailMessage email) ;
}
