package com.boky.PFE.service;

import com.boky.PFE.entite.Message;
import com.boky.PFE.exceptions.NoChatExistsInTheRepository;

import java.util.List;

public interface ChatMessages {

    List<Message> getAllMessagesInChat(int chatId) throws NoChatExistsInTheRepository;
}
