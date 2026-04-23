package com.boky.PFE.datasource;

import com.boky.PFE.entite.Chat;
import com.boky.PFE.service.ChatDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Qualifier("chatCache")
public class CachedChatDataSource implements ChatDataSource {

    private final ChatDataSource delegate;
    private final Map<Integer, Chat> cache = new ConcurrentHashMap<>();

    public CachedChatDataSource(@Qualifier("chatJpa") ChatDataSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public Chat save(Chat chat) {
        Chat saved = delegate.save(chat);
        cache.put(saved.getChatId(), saved);
        return saved;
    }

    @Override
    public Optional<Chat> findById(int id) {
        if (cache.containsKey(id)) {
            return Optional.of(cache.get(id));
        }
        Optional<Chat> result = delegate.findById(id);
        result.ifPresent(c -> cache.put(c.getChatId(), c));
        return result;
    }

    @Override
    public List<Chat> findAll() {
        List<Chat> all = delegate.findAll();
        all.forEach(c -> cache.put(c.getChatId(), c));
        return all;
    }

    @Override
    public HashSet<Chat> findByFirstUserName(String username) {
        return delegate.findByFirstUserName(username);
    }

    @Override
    public HashSet<Chat> findBySecondUserName(String username) {
        return delegate.findBySecondUserName(username);
    }

    @Override
    public HashSet<Chat> findByUsers(String first, String second) {
        return delegate.findByUsers(first, second);
    }

    @Override
    public HashSet<Chat> findByUsersReversed(String first, String second) {
        return delegate.findByUsersReversed(first, second);
    }
}
