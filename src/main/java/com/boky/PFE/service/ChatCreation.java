package com.boky.PFE.service;

import com.boky.PFE.entite.Chat;
import com.boky.PFE.entite.Message;
import com.boky.PFE.exceptions.ChatAlreadyExistException;
import com.boky.PFE.exceptions.ChatNotFoundException;

public interface ChatCreation {

    Chat addChat(Chat chat) throws ChatAlreadyExistException;

    Chat addMessage(Message msg, int chatId) throws ChatNotFoundException;

    Message addMessage2(Message message);
}
