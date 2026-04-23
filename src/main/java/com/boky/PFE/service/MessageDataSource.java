package com.boky.PFE.service;

import com.boky.PFE.entite.Message;

import java.util.Optional;


public interface MessageDataSource {

    Message save(Message message);

    Optional<Message> findById(int id);
}
