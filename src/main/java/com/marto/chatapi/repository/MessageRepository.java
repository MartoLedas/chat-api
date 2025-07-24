package com.marto.chatapi.repository;

import com.marto.chatapi.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT * FROM messages ORDER BY created_at DESC", nativeQuery = true)
    List<Message> findAllOrderByCreatedAtDesc();

    @Modifying
    @Transactional
    @Query(value = "UPDATE messages SET user_id = NULL WHERE user_id = :userId", nativeQuery = true)
    void updateMessagesToAnonymous(@Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) FROM messages WHERE user_id = :userId", nativeQuery = true)
    long countByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT MIN(created_at) FROM messages WHERE user_id = :userId", nativeQuery = true)
    LocalDateTime findFirstMessageTimeByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT MAX(created_at) FROM messages WHERE user_id = :userId", nativeQuery = true)
    LocalDateTime findLastMessageTimeByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT AVG(LENGTH(content)) FROM messages WHERE user_id = :userId", nativeQuery = true)
    Double findAverageMessageLengthByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT content FROM messages WHERE user_id = :userId ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    String findLastMessageContentByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT m.*, u.username FROM messages m LEFT JOIN users u ON m.user_id = u.id ORDER BY m.created_at DESC", nativeQuery = true)
    List<Object[]> findMessagesWithUsernames();
}
