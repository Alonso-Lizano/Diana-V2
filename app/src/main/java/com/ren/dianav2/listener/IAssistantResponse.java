package com.ren.dianav2.listener;

import com.ren.dianav2.assistants.models.response.AssistantResponse;

public interface IAssistantResponse {
    void didFetch(AssistantResponse assistantResponse, String msg);
    void didError(String msg);
}
