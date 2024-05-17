package com.ren.dianav2.listener;

import com.ren.dianav2.models.text.TextResponse;

public interface ITextResponse {

    void didFetch(TextResponse text, String msg);
    void didError(String msg);

}
