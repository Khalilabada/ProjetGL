package com.boky.PFE.service;


import com.boky.PFE.entite.Chat;
import com.boky.PFE.entite.Message;
import com.boky.PFE.exceptions.ChatNotFoundException;
import com.boky.PFE.exceptions.NoChatExistsInTheRepository;
import com.boky.PFE.repository.ChatRepository;
import com.boky.PFE.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Chat addChat(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public Message addMessage2(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getAllMessagesInChat(int chatId) throws NoChatExistsInTheRepository {
        Optional<Chat> chat = chatRepository.findById(chatId);

        if(chat.isEmpty()){
            throw new NoChatExistsInTheRepository();
        }else {
            return chat.get().getMessageList();
        }
    }

    @Override
    public List<Chat> findallchats() throws NoChatExistsInTheRepository {
        if (chatRepository.findAll().isEmpty()) {
            throw new NoChatExistsInTheRepository();
        } else {
            return chatRepository.findAll();
        }

    }

    @Override
    public Chat getById(int id) throws ChatNotFoundException {
        Optional<Chat> chatid = chatRepository.findById(id);
        if (chatid.isPresent()) {
            return chatid.get();
        } else {
            throw new ChatNotFoundException();
        }
    }

    @Override
    public HashSet<Chat> getChatByFirstUserName(String username) throws ChatNotFoundException {

        HashSet<Chat> chat = chatRepository.getChatByEmailfirstUserName(username);

        if (chat.isEmpty()) {
            throw new ChatNotFoundException();
        } else {
            return chat;
        }
    }

    @Override
    public HashSet<Chat> getChatBySecondUserName(String username) throws ChatNotFoundException {
        HashSet<Chat> chat = chatRepository.getChatByEmailSecondeUser(username);
        if (chat.isEmpty()) {
            throw new ChatNotFoundException();
        } else {
            return chat;
        }
    }

    @Override
    public HashSet<Chat> getChatByFirstUserNameOrSecondUserName(String username) throws ChatNotFoundException {
        HashSet<Chat> chat = chatRepository.getChatByEmailfirstUserName(username);
        HashSet<Chat> chat1 = chatRepository.getChatByEmailSecondeUser(username);

        chat1.addAll(chat);

        if (chat.isEmpty() && chat1.isEmpty()) {
            throw new ChatNotFoundException();
        } else if (chat1.isEmpty()) {
            return chat;
        } else {
            return chat1;
        }
    }

    @Override
    public Chat getChatByFirstUserNameAndSecondUserName(String firstUserName, String secondUserName) throws ChatNotFoundException {
        HashSet<Chat> chat = chatRepository.getChatByEmailfirstUserNameAndEmailSecondeUser(firstUserName, secondUserName);
        HashSet<Chat> chat1 = chatRepository.getChatByEmailSecondeUserAndEmailfirstUserName(firstUserName, secondUserName);
System.out.println("chat hathy "+ chat);
        System.out.println("chat1 hathy "+ chat1);

        if (chat.isEmpty() && chat1.isEmpty()) {
            throw new ChatNotFoundException();
        } else if (!chat.isEmpty()) {
            // Si chat n'est pas vide, renvoyer le premier chat trouvé
            return chat.iterator().next();
        } else {
            // Sinon, renvoyer le premier chat trouvé dans chat1
            return chat1.iterator().next();
        }
    }

    @Override
    public Chat addMessage(Message add, int chatId) throws ChatNotFoundException {
        Optional<Chat> chat=chatRepository.findById(chatId);
        Chat abc=chat.get();

        if(abc.getMessageList()==null){
            List<Message> msg=new ArrayList<>();
            msg.add(add);
            abc.setMessageList(msg);
            return chatRepository.save(abc);
        }else{
            List<Message> rates=abc.getMessageList();
            rates.add(add);
            abc.setMessageList(rates);
            return chatRepository.save(abc);
        }
    }



}
