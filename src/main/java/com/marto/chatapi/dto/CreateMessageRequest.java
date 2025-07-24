package com.marto.chatapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessageRequest {
    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Message content cannot exceed 1000 characters")
    private String content;

    @NotBlank(message = "Username is required")
    private String username;
}
