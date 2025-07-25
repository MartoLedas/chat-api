package com.marto.chatapi.service;

import com.marto.chatapi.dto.CreateMessageRequest;
import com.marto.chatapi.dto.MessageResponse;
import com.marto.chatapi.model.Message;
import com.marto.chatapi.model.User;
import com.marto.chatapi.repository.MessageRepository;
import com.marto.chatapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<MessageResponse> getAllMessages() {
        List<Message> messages = messageRepository.findAllOrderByCreatedAtDesc();

        return messages.stream()
                .map(message -> {
                    final String username = message.getUserId() == null
                            ? "anonymous"
                            : userRepository.findById(message.getUserId())
                            .map(User::getUsername)
                            .orElse("anonymous");

                    return new MessageResponse(
                            message.getId(),
                            username,
                            message.getContent(),
                            message.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

    }

    public MessageResponse createMessage(CreateMessageRequest request) {
        Long userId = request.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id '" + userId + "' not found"));

        Message message = new Message(user.getId(), request.getContent());
        Message savedMessage = messageRepository.save(message);

        return new MessageResponse(
                savedMessage.getId(),
                savedMessage.getContent(),
                user.getUsername(),
                savedMessage.getCreatedAt()
        );
    }

}
