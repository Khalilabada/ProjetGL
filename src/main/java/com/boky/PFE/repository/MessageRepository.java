package com.boky.PFE.repository;

import com.boky.PFE.entite.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
