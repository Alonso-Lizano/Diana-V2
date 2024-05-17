package com.ren.dianav2.listener;

import okhttp3.ResponseBody;

public interface IResponseBody {

    void didFetch(ResponseBody responseBody, String msg);
    void didError(String msg);

}
