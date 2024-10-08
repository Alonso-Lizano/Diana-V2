package com.ren.dianav2.assistants.models.response;

import com.ren.dianav2.assistants.models.Metadata;
import com.ren.dianav2.assistants.models.Tool;

import java.util.ArrayList;

public class AssistantResponse {
    public String id;
    public String object;
    public int created_at;
    public String name;
    public Object description;
    public String model;
    public String instructions;
    public ArrayList<Tool> tools;
    public Metadata metadata;
    public double top_p;
    public double temperature;
    public String response_format;
}
