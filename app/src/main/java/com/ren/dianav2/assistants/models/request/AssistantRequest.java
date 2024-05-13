package com.ren.dianav2.assistants.models.request;

import com.ren.dianav2.assistants.models.Tool;

import java.util.ArrayList;

public class AssistantRequest {

    public String instructions;
    public String name;
    public ArrayList<Tool> tools;
    public String model;
    public String description;

}
