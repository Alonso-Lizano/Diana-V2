package com.ren.dianav2.listener;

import com.ren.dianav2.assistants.models.response.ListMessageResponse;

public interface IListMessageResponse {
    void didFetch(ListMessageResponse listMessageResponse, String msg);
    void didError(String msg);
}
