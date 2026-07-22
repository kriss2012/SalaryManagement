package com.one.project.Employee;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderName;
    private String senderAvatar;
    private String messageText;
    private LocalDateTime timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ChatMessage(String senderName, String senderAvatar, String messageText, LocalDateTime timestamp) {
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public ChatMessage() {
    }
}
