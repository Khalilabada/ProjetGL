package com.boky.PFE.service;

import com.boky.PFE.entite.Chat;
import com.boky.PFE.entite.Message;
import com.boky.PFE.exceptions.ChatAlreadyExistException;
import com.boky.PFE.exceptions.ChatNotFoundException;
import com.boky.PFE.exceptions.NoChatExistsInTheRepository;

import java.util.HashSet;
import java.util.List;

public interface ChatService
{
    public Chat addChat(Chat chat) throws ChatAlreadyExistException;

    List<Chat> findallchats() throws NoChatExistsInTheRepository;

    Chat getById(int id)  throws ChatNotFoundException;

    HashSet<Chat> getChatByFirstUserName(String username)  throws ChatNotFoundException;

    HashSet<Chat> getChatBySecondUserName(String username)  throws ChatNotFoundException;

    HashSet<Chat> getChatByFirstUserNameOrSecondUserName(String username)  throws ChatNotFoundException;

    Chat getChatByFirstUserNameAndSecondUserName(String firstUserName, String secondUserName)  throws ChatNotFoundException;

    Chat addMessage(Message add, int chatId)  throws ChatNotFoundException;

    Message addMessage2(Message message);

    List<Message> getAllMessagesInChat(int chatId) throws NoChatExistsInTheRepository;
}
