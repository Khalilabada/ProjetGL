package com.boky.PFE.repository;

import com.boky.PFE.entite.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnonceRepository extends JpaRepository<Annonce,Long> {
    List<Annonce> findByAnnonceurId(Long id);


}
