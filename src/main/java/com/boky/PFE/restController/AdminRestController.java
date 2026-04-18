package com.boky.PFE.restController;

import com.boky.PFE.entite.Admin;
import com.boky.PFE.repository.AdminRepository;
import com.boky.PFE.service.AdminService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Admin")
public class AdminRestController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminService adminService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> ajouterAdmin(@RequestBody Admin admin) {
        HashMap<String, Object> response = new HashMap<>();
        if (adminRepository.existsByEmail(admin.getEmail())) {
            response.put("message", "Email existant déjà !");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            admin.setMdp(bCryptPasswordEncoder.encode(admin.getMdp()));
            Admin savedUser = adminRepository.save(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Admin> AfficherAdmin() {
        return adminService.AfficherAdmin();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> SupprimerAdmin(@PathVariable("id") Long id) {
        adminService.SupprimerAdmin(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/Login")
    public ResponseEntity<Map<String, Object>> loginAdmin(@RequestBody Admin admin) {
        System.out.println("in login-admin: " + admin);
        HashMap<String, Object> response = new HashMap<>();
        Admin userFromDB = adminRepository.findAdminByEmail(admin.getEmail());
        System.out.println("userFromDB: " + userFromDB);

        if (userFromDB == null) {
            response.put("message", "Admin non trouvé !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        boolean compare = bCryptPasswordEncoder.matches(admin.getMdp(), userFromDB.getMdp());
        System.out.println("compare: " + compare);

        if (!compare) {
            response.put("message", "Mot de passe incorrect !");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = Jwts.builder()
                .claim("data", userFromDB)
                .signWith(SignatureAlgorithm.HS256, "SECRET")
                .compact();

        response.put("token", token);
        response.put("role", userFromDB.getRole());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Admin> modifierAdmin(@PathVariable("id") Long id, @RequestBody Admin admin) {
        Optional<Admin> existingAdminOpt = adminRepository.findById(id);

        if (existingAdminOpt.isPresent()) {
            Admin existingAdmin = existingAdminOpt.get();

            existingAdmin.setNom(admin.getNom());
            existingAdmin.setPrenom(admin.getPrenom());
            existingAdmin.setEmail(admin.getEmail());

            // Only update and encode the password if the new password is different from the old one
            if (!admin.getMdp().equals(existingAdmin.getMdp())) {
                existingAdmin.setMdp(bCryptPasswordEncoder.encode(admin.getMdp()));
            }

            existingAdmin.setRole(admin.getRole());
            existingAdmin.setPhoto(admin.getPhoto());

            Admin updatedAdmin = adminRepository.save(existingAdmin);
            return ResponseEntity.ok(updatedAdmin);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Optional<Admin>> getAdminById(@PathVariable("id") long id) {
        Optional<Admin> admin = adminService.getAdminById(id);
        if (admin.isPresent()) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
