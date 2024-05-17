package com.ren.dianav2.models.text;

import com.ren.dianav2.models.text.Message;

import java.util.List;

public class TextRequest {

    private String model;
    private List<Message> messages;

    public TextRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
