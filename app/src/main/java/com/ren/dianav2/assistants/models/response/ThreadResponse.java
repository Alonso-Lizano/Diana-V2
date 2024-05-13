package com.ren.dianav2.assistants.models.response;

import com.ren.dianav2.assistants.models.Metadata;
import com.ren.dianav2.assistants.models.ToolResources;

public class ThreadResponse {
    public String id;
    public String object;
    public int created_at;
    public Metadata metadata;
    public ToolResources tool_resources;
}
