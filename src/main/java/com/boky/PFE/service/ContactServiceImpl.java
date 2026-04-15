package com.boky.PFE.service;

import com.boky.PFE.entite.Contact;
import com.boky.PFE.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ContactServiceImpl implements ContactService{
    @Autowired
    ContactRepository contactRepository;
    @Override
    public Contact AjouterContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public void SupprimerContact(Long id) {
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> AfficherContact() {
        return contactRepository.findAll();
    }

    @Override
    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }
}
