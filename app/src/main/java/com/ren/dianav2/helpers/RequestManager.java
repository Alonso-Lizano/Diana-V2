package com.ren.dianav2.helpers;

import android.content.Context;

import com.ren.dianav2.R;
import com.ren.dianav2.listener.IHttpRequest;
import com.ren.dianav2.listener.IImageResponse;
import com.ren.dianav2.models.request.images.ImageRequest;
import com.ren.dianav2.models.response.images.ImageResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {

    private Context context;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void generateImage(ImageRequest imageRequest, final IImageResponse imageResponse) {
        IHttpRequest request = retrofit.create(IHttpRequest.class);
        Call<ImageResponse> call = request.generateImage(context.getString(R.string.api_key), imageRequest);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (!response.isSuccessful()) {
                    imageResponse.didError(response.message());
                    return;
                }
                imageResponse.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable throwable) {
                imageResponse.didError(throwable.getMessage());
            }
        });
    }
}
