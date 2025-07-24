package com.marto.chatapi.controller;

import com.marto.chatapi.dto.CreateMessageRequest;
import com.marto.chatapi.dto.MessageResponse;
import com.marto.chatapi.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Chat Messages", description = "Operations for chat messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @Operation(summary = "Get all messages", description = "Get all chat messages sorted from newest to oldest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<MessageResponse>> getAllMessages() {
        List<MessageResponse> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    @Operation(summary = "Send a new message", description = "Users can send a new message to the public chat room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid message data or user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody CreateMessageRequest request) {
        try {
            MessageResponse response = messageService.createMessage(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}