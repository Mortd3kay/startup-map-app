package com.skyletto.startappbackend.services;

import com.skyletto.startappbackend.entities.Message;
import com.skyletto.startappbackend.entities.User;
import com.skyletto.startappbackend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChat(User user, Long chatId, Long lastId) {
        List<Message> messages = messageRepository.getMessagesByUsers(user.getId(), chatId, lastId);
        return messages;
    }

    public List<Message> getLastMessages(User user){
        return messageRepository.getLastMessages(user.getId());
    }
}
