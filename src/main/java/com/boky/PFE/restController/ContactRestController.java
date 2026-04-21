package com.boky.PFE.restController;

import com.boky.PFE.entite.Contact;
import com.boky.PFE.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Contact")
public class ContactRestController {

    private final ContactService contactService;

    public ContactRestController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Contact> AjouterContact(@RequestBody Contact contact) {
        Contact savedUser = contactService.AjouterContact(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Contact> AfficherContact() {
        return contactService.AfficherContact();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void SupprimerContact(@PathVariable("id") Long id) {
        contactService.SupprimerContact(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Contact> getContactById(@PathVariable("id") long id) {
        return contactService.getContactById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Contact ModifierContact(@RequestBody Contact contact, @PathVariable("id") Long id) {
        return contactService.modifierContactAvecReponse(contact, id);
    }
}
