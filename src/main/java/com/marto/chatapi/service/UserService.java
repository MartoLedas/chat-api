package com.marto.chatapi.service;

import com.marto.chatapi.dto.CreateUserRequest;
import com.marto.chatapi.dto.UserResponse;
import com.marto.chatapi.dto.UserStatisticsResponse;
import com.marto.chatapi.model.User;
import com.marto.chatapi.repository.MessageRepository;
import com.marto.chatapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    public UserService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("User with username '" + request.getUsername() + "' already exists");
        }

        User.UserRole role;
        try {
            role = User.UserRole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role. Must be 'USER' or 'ADMIN'");
        }

        User user = new User(request.getUsername(), request.getPassword(), role);
        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole().toString()
        );
    }

    @Transactional
    public void deleteUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with id '" + userId + "' not found");
        }

        User user = userOpt.get();

        // update all messages from this user to anonymous (userId = null)
        messageRepository.updateMessagesToAnonymous(user.getId());

        userRepository.delete(user);
    }

    public UserStatisticsResponse getUserStatistics(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with id '" + userId + "' not found");
        }

        User user = userOpt.get();

        long messageCount = messageRepository.countByUserId(userId);
        LocalDateTime firstMessageTime = messageRepository.findFirstMessageTimeByUserId(userId);
        LocalDateTime lastMessageTime = messageRepository.findLastMessageTimeByUserId(userId);
        Double averageMessageLength = messageRepository.findAverageMessageLengthByUserId(userId);
        String lastMessageContent = messageRepository.findLastMessageContentByUserId(userId);

        return new UserStatisticsResponse(
                user.getUsername(),
                messageCount,
                firstMessageTime,
                lastMessageTime,
                averageMessageLength != null ? averageMessageLength : 0.0,
                lastMessageContent
        );
    }
}
