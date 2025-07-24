package com.marto.chatapi.service;

import com.marto.chatapi.dto.CreateMessageRequest;
import com.marto.chatapi.dto.MessageResponse;
import com.marto.chatapi.model.Message;
import com.marto.chatapi.model.User;
import com.marto.chatapi.repository.MessageRepository;
import com.marto.chatapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
        List<Object[]> results = messageRepository.findMessagesWithUsernames();

        return results.stream()
                .map(result -> {
                    Long id = ((BigInteger) result[0]).longValue();
                    Long userId = result[1] != null ? ((BigInteger) result[1]).longValue() : null;
                    String content = (String) result[2];
                    LocalDateTime createdAt = ((Timestamp) result[3]).toLocalDateTime();
                    String username = result[4] != null ? (String) result[4] : "anonymous";

                    return new MessageResponse(id, content, username, createdAt);
                })
                .collect(Collectors.toList());
    }

    public MessageResponse createMessage(CreateMessageRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with username '" + request.getUsername() + "' not found");
        }

        User user = userOpt.get();
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
