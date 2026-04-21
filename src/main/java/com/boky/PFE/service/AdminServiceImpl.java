package com.boky.PFE.service;

import com.boky.PFE.entite.Admin;
import com.boky.PFE.repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AdminServiceImpl(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

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

    @Override
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    @Override
    public Admin findAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }

    @Override
    public ResponseEntity<?> creerAdminSiEmailDisponible(Admin admin) {
        HashMap<String, Object> response = new HashMap<>();
        if (adminRepository.existsByEmail(admin.getEmail())) {
            response.put("message", "Email existant déjà !");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        admin.setMdp(passwordEncoder.encode(admin.getMdp()));
        Admin savedUser = adminRepository.save(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Override
    public ResponseEntity<Admin> mettreAJourAdmin(Long id, Admin admin) {
        Optional<Admin> existingAdminOpt = adminRepository.findById(id);
        if (existingAdminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Admin existingAdmin = existingAdminOpt.get();
        existingAdmin.setNom(admin.getNom());
        existingAdmin.setPrenom(admin.getPrenom());
        existingAdmin.setEmail(admin.getEmail());
        if (!admin.getMdp().equals(existingAdmin.getMdp())) {
            existingAdmin.setMdp(passwordEncoder.encode(admin.getMdp()));
        }
        existingAdmin.setRole(admin.getRole());
        existingAdmin.setPhoto(admin.getPhoto());
        Admin updatedAdmin = adminRepository.save(existingAdmin);
        return ResponseEntity.ok(updatedAdmin);
    }

    @Override
    public ResponseEntity<Map<String, Object>> loginAdmin(Admin admin) {
        HashMap<String, Object> response = new HashMap<>();
        Admin userFromDB = adminRepository.findAdminByEmail(admin.getEmail());
        if (userFromDB == null) {
            response.put("message", "Admin non trouvé !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (!passwordEncoder.matches(admin.getMdp(), userFromDB.getMdp())) {
            response.put("message", "Mot de passe incorrect !");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String token = jwtTokenService.createTokenForUserData(userFromDB);
        response.put("token", token);
        response.put("role", userFromDB.getRole());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
