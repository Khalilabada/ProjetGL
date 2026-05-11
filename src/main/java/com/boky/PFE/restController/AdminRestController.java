package com.boky.PFE.restController;

import com.boky.PFE.entite.Admin;
import com.boky.PFE.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/Admin")
public class AdminRestController {

    private final AdminService adminService;

    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> ajouterAdmin(@RequestBody Admin admin) {
        return adminService.creerAdminSiEmailDisponible(admin);
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
        return adminService.loginAdmin(admin);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Admin> modifierAdmin(@PathVariable("id") Long id, @RequestBody Admin admin) {
        return adminService.mettreAJourAdmin(id, admin);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Optional<Admin>> getAdminById(@PathVariable("id") long id) {
        Optional<Admin> admin = adminService.getAdminById(id);
        if (admin.isPresent()) {
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
