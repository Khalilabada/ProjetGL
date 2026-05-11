package com.boky.PFE.service;

import com.boky.PFE.entite.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService
{
    Contact AjouterContact(Contact contact);
    void SupprimerContact (Long id);
    List<Contact> AfficherContact();
    Optional<Contact> getContactById(Long id);

    /**
     * Met à jour un contact, envoie la notification de réponse puis persiste.
     * La logique reste dans la couche service (DIP : le contrôleur ne dépend pas du repository).
     */
    Contact modifierContactAvecReponse(Contact contact, Long id);

}
