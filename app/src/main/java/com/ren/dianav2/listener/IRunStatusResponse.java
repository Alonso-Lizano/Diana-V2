package com.ren.dianav2.listener;

import com.ren.dianav2.assistants.models.response.RunResponse;
import com.ren.dianav2.assistants.models.response.RunStatusResponse;

public interface IRunStatusResponse {
    void didFetch(RunStatusResponse runStatusResponse, String msg);
    void didError(String msg);
}
