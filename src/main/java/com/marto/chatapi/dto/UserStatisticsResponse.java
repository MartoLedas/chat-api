package com.marto.chatapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsResponse {
    private String username;
    private long messageCount;
    private LocalDateTime firstMessageTime;
    private LocalDateTime lastMessageTime;
    private Double averageMessageLength;
    private String lastMessageContent;
}
