package com.ren.dianav2.listener;

import com.ren.dianav2.models.request.images.ImageRequest;
import com.ren.dianav2.models.response.images.ImageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IHttpRequest {

    @POST("images/generations")
    Call<ImageResponse> generateImage(
            @Header("Authorization") String authorization,
            @Body ImageRequest imageRequest
    );

}
