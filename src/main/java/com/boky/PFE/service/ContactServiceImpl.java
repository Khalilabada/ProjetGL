package com.boky.PFE.service;

import com.boky.PFE.entite.Contact;
import com.boky.PFE.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final NotificationService notificationService;

    public ContactServiceImpl(ContactRepository contactRepository, NotificationService notificationService) {
        this.contactRepository = contactRepository;
        this.notificationService = notificationService;
    }
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

    @Override
    public Contact modifierContactAvecReponse(Contact contact, Long id) {
        Optional<Contact> existing = contactRepository.findById(id);
        if (existing.isEmpty()) {
            return null;
        }
        Contact contact1 = existing.get();
        contact1.setId(contact.getId());
        contact1.setEmail(contact.getEmail());
        contact1.setSujet(contact.getSujet());
        contact1.setMsg(contact.getMsg());
        contact1.setTelephone(contact.getTelephone());
        contact1.setRepondre(contact.getRepondre());
        notificationService.notifyContactResponse(contact1);
        return contactRepository.save(contact1);
    }
}
