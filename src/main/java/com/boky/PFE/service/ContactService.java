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

}
