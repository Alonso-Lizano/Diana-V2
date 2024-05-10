package com.ren.dianav2.models;

public class Item {

    private int ivAssistant;
    private String tvTitle;
    private String tvDescription;

    public Item(int ivAssistant, String tvTitle, String tvDescription) {
        this.ivAssistant = ivAssistant;
        this.tvTitle = tvTitle;
        this.tvDescription = tvDescription;
    }

    public int getIvAssistant() {
        return ivAssistant;
    }

    public void setIvAssistant(int ivAssistant) {
        this.ivAssistant = ivAssistant;
    }

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }

    public String getTvDescription() {
        return tvDescription;
    }

    public void setTvDescription(String tvDescription) {
        this.tvDescription = tvDescription;
    }

}
