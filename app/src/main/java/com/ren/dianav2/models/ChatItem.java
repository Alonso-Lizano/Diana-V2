package com.ren.dianav2.models;

public class ChatItem {

    private int ivIcon;
    private String title;

    public ChatItem(int ivIcon, String title) {
        this.ivIcon = ivIcon;
        this.title = title;
    }

    public int getIvIcon() {
        return ivIcon;
    }

    public void setIvIcon(int ivIcon) {
        this.ivIcon = ivIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
