package com.ren.dianav2.assistants.models.request;

public class MessageRequest {

    public String role;
    public String content;

    public MessageRequest(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
