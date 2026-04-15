package com.boky.PFE.repository;

import com.boky.PFE.entite.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long>
{

    boolean existsByEmail(String email);

    Admin findAdminByEmail(String email);
}
