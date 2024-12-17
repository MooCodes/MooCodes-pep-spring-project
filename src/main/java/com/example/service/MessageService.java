package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }


    public Optional<Message> getMessageById(Integer messageId) {
        // Fetch the message from the database using the repository
        return messageRepository.findById(messageId);
    }

    public void deleteMessage(Integer messageId) {
        messageRepository.deleteById(messageId);
    }

    public void updateMessage(Integer messageId, String messageText) {
        messageRepository.updateMessageText(messageId, messageText);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
