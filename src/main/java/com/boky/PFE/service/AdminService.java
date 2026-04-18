package com.boky.PFE.service;

import com.boky.PFE.entite.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminService
{
    Admin AjouterAdmin(Admin admin);
    Admin ModifierAdmin(Admin admin);
    List<Admin> AfficherAdmin();
    void SupprimerAdmin (Long id);
    Optional<Admin> getAdminById(Long id);
}
