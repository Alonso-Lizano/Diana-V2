package com.ren.dianav2.listener;

import com.ren.dianav2.models.response.images.ImageResponse;

public interface IImageResponse {

    void didFetch(ImageResponse imageResponse, String msg);
    void didError(String msg);

}
