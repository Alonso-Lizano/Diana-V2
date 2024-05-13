package com.ren.dianav2.listener;

import com.ren.dianav2.assistants.models.response.ThreadResponse;

public interface IThreadResponse {
    void didFetch(ThreadResponse threadResponse, String msg);
    void didError(String msg);
}
