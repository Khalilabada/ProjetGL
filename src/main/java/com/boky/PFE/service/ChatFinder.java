package com.boky.PFE.service;

import com.boky.PFE.entite.Chat;
import com.boky.PFE.exceptions.ChatNotFoundException;
import com.boky.PFE.exceptions.NoChatExistsInTheRepository;

import java.util.HashSet;
import java.util.List;

public interface ChatFinder {

    Chat getById(int id) throws ChatNotFoundException;

    List<Chat> findallchats() throws NoChatExistsInTheRepository;

    HashSet<Chat> getChatByFirstUserName(String username) throws ChatNotFoundException;

    HashSet<Chat> getChatBySecondUserName(String username) throws ChatNotFoundException;

    HashSet<Chat> getChatByFirstUserNameOrSecondUserName(String username) throws ChatNotFoundException;

    Chat getChatByFirstUserNameAndSecondUserName(String first, String second) throws ChatNotFoundException;
}
