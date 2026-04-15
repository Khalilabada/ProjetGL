package com.boky.PFE.service;

import com.boky.PFE.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService
{
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Override
    public void SupprimerConfirmationToken(Long id) {
        confirmationTokenRepository.deleteById(id);
    }
}
