package com.boky.PFE.service;

import com.boky.PFE.entite.Chat;
import com.boky.PFE.entite.Message;
import com.boky.PFE.exceptions.ChatNotFoundException;
import com.boky.PFE.exceptions.NoChatExistsInTheRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service
public class ChatServiceImpl implements ChatCreation, ChatFinder, ChatMessages {

    private final ChatDataSource chatDataSource;
    private final MessageDataSource messageDataSource;

    public ChatServiceImpl(ChatDataSource chatDataSource, MessageDataSource messageDataSource) {
        this.chatDataSource = chatDataSource;
        this.messageDataSource = messageDataSource;
    }

    // ── ChatCreation ──────────────────────────────────────────────────────────

    @Override
    public Chat addChat(Chat chat) {
        return chatDataSource.save(chat);
    }

    @Override
    public Chat addMessage(Message add, int chatId) throws ChatNotFoundException {
        Optional<Chat> optChat = chatDataSource.findById(chatId);

        if (optChat.isEmpty()) {
            throw new ChatNotFoundException();
        }

        Chat chat = optChat.get();
        chat.addMessage(add);

        return chatDataSource.save(chat);
    }

    @Override
    public Message addMessage2(Message message) {
        return messageDataSource.save(message);
    }

    // ── ChatFinder ────────────────────────────────────────────────────────────

    @Override
    public Chat getById(int id) throws ChatNotFoundException {
        Optional<Chat> chat = chatDataSource.findById(id);
        if (chat.isPresent()) {
            return chat.get();
        } else {
            throw new ChatNotFoundException();
        }
    }

    @Override
    public List<Chat> findallchats() throws NoChatExistsInTheRepository {
        List<Chat> all = chatDataSource.findAll();
        if (all.isEmpty()) {
            throw new NoChatExistsInTheRepository();
        }
        return all;
    }

    @Override
    public HashSet<Chat> getChatByFirstUserName(String username) throws ChatNotFoundException {
        HashSet<Chat> chat = chatDataSource.findByFirstUserName(username);
        if (chat.isEmpty()) {
            throw new ChatNotFoundException();
        }
        return chat;
    }

    @Override
    public HashSet<Chat> getChatBySecondUserName(String username) throws ChatNotFoundException {
        HashSet<Chat> chat = chatDataSource.findBySecondUserName(username);
        if (chat.isEmpty()) {
            throw new ChatNotFoundException();
        }
        return chat;
    }

    @Override
    public HashSet<Chat> getChatByFirstUserNameOrSecondUserName(String username) throws ChatNotFoundException {
        HashSet<Chat> chat  = chatDataSource.findByFirstUserName(username);
        HashSet<Chat> chat1 = chatDataSource.findBySecondUserName(username);

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
    public Chat getChatByFirstUserNameAndSecondUserName(String firstUserName, String secondUserName)
            throws ChatNotFoundException {

        HashSet<Chat> chat  = chatDataSource.findByUsers(firstUserName, secondUserName);
        HashSet<Chat> chat1 = chatDataSource.findByUsersReversed(firstUserName, secondUserName);
        System.out.println("chat hathy " + chat);
        System.out.println("chat1 hathy " + chat1);

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

    // ── ChatMessages ──────────────────────────────────────────────────────────

    @Override
    public List<Message> getAllMessagesInChat(int chatId) throws NoChatExistsInTheRepository {
        Optional<Chat> chat = chatDataSource.findById(chatId);

        if (chat.isEmpty()) {
            throw new NoChatExistsInTheRepository();
        } else {
            return chat.get().getMessageList();
        }
    }
}
