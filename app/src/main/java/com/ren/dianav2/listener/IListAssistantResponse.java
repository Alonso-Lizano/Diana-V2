package com.ren.dianav2.listener;

import com.ren.dianav2.assistants.models.response.ListAssistantResponse;

public interface IListAssistantResponse {
    void didFetch(ListAssistantResponse listAssistantResponse, String msg);
    void didError(String msg);
}
