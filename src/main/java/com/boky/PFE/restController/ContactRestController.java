package com.boky.PFE.restController;

import com.boky.PFE.entite.Contact;
import com.boky.PFE.repository.ContactRepository;
import com.boky.PFE.service.ContactService;
import com.boky.PFE.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Contact")
public class ContactRestController {
    
    @Autowired
    ContactRepository contactRepository;
    
    @Autowired
    ContactService contactService;
    
    @Autowired
    EmailService emailService;
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Contact> AjouterContact(@RequestBody Contact contact) {
        Contact savedContact = contactRepository.save(contact);
        
        System.out.println("=========================================");
        System.out.println("📞 NOUVEAU CONTACT CRÉÉ");
        System.out.println("   ID: " + savedContact.getId());
        System.out.println("   Sujet: " + savedContact.getSujet());
        System.out.println("   Catégorie: " + savedContact.getCategorie());
        System.out.println("   Priorité: " + savedContact.getPriorite());
        System.out.println("   Email destination: " + savedContact.getEmailDestination());
        System.out.println("=========================================");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
    }
    
    @GetMapping("/categorie/{id}")
    public String getContactCategorie(@PathVariable("id") Long id) {
        Optional<Contact> contact = contactService.getContactById(id);
        if (contact.isPresent()) {
            Contact c = contact.get();
            return "Contact #" + id + " : " + c.getCategorie() + " - Priorité: " + c.getPriorite();
        }
        return "Contact non trouvé";
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
        Contact contact1 = contactRepository.findById(id).get();
        contact1.setId(contact.getId());
        contact1.setEmail(contact.getEmail());
        contact1.setSujet(contact.getSujet());
        contact1.setMsg(contact.getMsg());
        contact1.setTelephone(contact.getTelephone());
        contact1.setRepondre(contact.getRepondre());
        
        emailService.SendSimpleMessage(
            contact1.getEmail(),
            "Réponse concernant le sujet : " + contact1.getSujet(),
            contact1.getRepondre()
        );
        
        return contactRepository.save(contact1);
    }
}