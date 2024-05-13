package com.ren.dianav2.listener;

import com.ren.dianav2.assistants.models.response.RunResponse;

public interface IRunResponse {
    void didFetch(RunResponse runResponse, String msg);
    void didError(String msg);
}
