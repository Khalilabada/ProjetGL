package com.boky.PFE.restController;

import com.boky.PFE.entite.Contact;
import com.boky.PFE.entite.Utilisateur;
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
public class ContactRestController
{
    @Autowired
    ContactRepository contactRepository;
    @RequestMapping(method = RequestMethod.POST )
    public ResponseEntity<Contact> AjouterContact (@RequestBody Contact contact)
    {
            Contact savedUser = contactRepository.save(contact);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

    }
    @Autowired
    ContactService contactService;
    @Autowired
    EmailService emailService;
    @RequestMapping(method = RequestMethod.GET)
    public List<Contact> AfficherContact()
    {
        return contactService.AfficherContact();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE )

    public void SupprimerContact(@PathVariable("id") Long id){
        contactService.SupprimerContact(id);

    }
    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    public Optional<Contact> getContactById(@PathVariable("id") long id){

        Optional<Contact> contact = contactService.getContactById(id);
        return contact;
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Contact ModifierContact(@RequestBody Contact contact, @PathVariable("id") Long id) {
        Contact newContact = null;

            Contact contact1 = contactRepository.findById(id).get();
            var contactid = contact.getId();
            var email = contact.getEmail();
            var sujet = contact.getSujet();
            var msg = contact.getMsg();
            var telephone = contact.getTelephone();
            var repondre = contact.getRepondre();
            contact1.setId(contactid);
            contact1.setEmail(email);
            contact1.setSujet(sujet);
            contact1.setMsg(msg);
            contact1.setTelephone(telephone);
            contact1.setRepondre(repondre);



//mta3 yjih mail fih l etat

                //ternary expression

                emailService.SendSimpleMessage(contact1.getEmail(),"Réponse concernant le sujet :"+contact1.getSujet() , contact1.getRepondre());


            newContact = contactRepository.save(contact1);

        return newContact;
    }
}
