package com.ren.dianav2.models.text;

public class Message {
    public static String TYPE_TYPING = "typing";

    private String role;
    private String content;

    public Message(String message, String sentBy) {
        this.content = message;
        this.role = sentBy;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}