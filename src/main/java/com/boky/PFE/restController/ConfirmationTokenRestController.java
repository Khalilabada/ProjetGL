package com.boky.PFE.restController;

import com.boky.PFE.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/ConfirmationToken")
public class ConfirmationTokenRestController
{
    @Autowired
    ConfirmationTokenService confirmationTokenService;
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE )

    public void SupprimerConfirmationToken(@PathVariable("id") Long id){
        confirmationTokenService.SupprimerConfirmationToken(id);

    }
}
