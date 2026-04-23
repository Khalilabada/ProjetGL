package com.boky.PFE.datasource;

import com.boky.PFE.entite.Message;
import com.boky.PFE.repository.MessageRepository;
import com.boky.PFE.service.MessageDataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public class MessageJpaDataSource implements MessageDataSource {

    private final MessageRepository messageRepository;

    public MessageJpaDataSource(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Optional<Message> findById(int id) {
        return messageRepository.findById(id);
    }
}
