package com.boky.PFE.service;

import com.boky.PFE.entite.Chat;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;


public interface ChatDataSource {

    Chat save(Chat chat);

    Optional<Chat> findById(int id);

    List<Chat> findAll();

    HashSet<Chat> findByFirstUserName(String username);

    HashSet<Chat> findBySecondUserName(String username);

    HashSet<Chat> findByUsers(String first, String second);

    HashSet<Chat> findByUsersReversed(String first, String second);
}
