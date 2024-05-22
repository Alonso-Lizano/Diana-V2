package com.ren.dianav2.assistants.models;

import com.ren.dianav2.assistants.models.request.MessageRequest;

import java.util.List;

public class Conversation {
    private String id;
    private String userId;
    private List<MessageRequest> messages;

    public Conversation() {
    }

    public Conversation(String id, String userId, List<MessageRequest> messages) {
        this.id = id;
        this.userId = userId;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<MessageRequest> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageRequest> messages) {
        this.messages = messages;
    }
}
