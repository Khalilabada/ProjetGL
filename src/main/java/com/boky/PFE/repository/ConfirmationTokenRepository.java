package com.boky.PFE.repository;

import com.boky.PFE.entite.ConfirmationToken;
import com.boky.PFE.entite.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long>
{
    ConfirmationToken findByConfirmationToken(String confirmationToken);


    ConfirmationToken findByUtilisateur(Utilisateur utilisateur);
}
