package com.boky.PFE.service;

import com.boky.PFE.entite.Admin;
import com.boky.PFE.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AdminServiceImpl implements AdminService
{
@Autowired
    AdminRepository adminRepository;
    @Override
    public Admin AjouterAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin ModifierAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> AfficherAdmin() {
        return adminRepository.findAll();
    }

    @Override
    public void SupprimerAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }
}
