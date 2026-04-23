package com.boky.PFE.datasource;

import com.boky.PFE.entite.Chat;
import com.boky.PFE.repository.ChatRepository;
import com.boky.PFE.service.ChatDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Repository
@Primary
@Qualifier("chatJpa")
public class ChatJpaDataSource implements ChatDataSource {

    private final ChatRepository chatRepository;

    public ChatJpaDataSource(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public Optional<Chat> findById(int id) {
        return chatRepository.findById(id);
    }

    @Override
    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    @Override
    public HashSet<Chat> findByFirstUserName(String username) {
        return chatRepository.getChatByEmailfirstUserName(username);
    }

    @Override
    public HashSet<Chat> findBySecondUserName(String username) {
        return chatRepository.getChatByEmailSecondeUser(username);
    }

    @Override
    public HashSet<Chat> findByUsers(String first, String second) {
        return chatRepository.getChatByEmailfirstUserNameAndEmailSecondeUser(first, second);
    }

    @Override
    public HashSet<Chat> findByUsersReversed(String first, String second) {
        return chatRepository.getChatByEmailSecondeUserAndEmailfirstUserName(first, second);
    }
}
