package com.ren.dianav2.listener;

import com.ren.dianav2.assistants.models.response.MessageResponse;
import com.ren.dianav2.models.Message;

public interface IMessageResponse {
    void didFetch(MessageResponse messageResponse, String msg);
    void didError(String msg);
}
