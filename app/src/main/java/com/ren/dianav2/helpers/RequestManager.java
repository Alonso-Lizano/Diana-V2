package com.ren.dianav2.helpers;

import android.content.Context;

import com.ren.dianav2.R;
import com.ren.dianav2.assistants.models.request.AssistantRequest;
import com.ren.dianav2.assistants.models.request.MessageRequest;
import com.ren.dianav2.assistants.models.request.RunRequest;
import com.ren.dianav2.assistants.models.request.ThreadRequest;
import com.ren.dianav2.assistants.models.response.AssistantResponse;
import com.ren.dianav2.assistants.models.response.ListAssistantResponse;
import com.ren.dianav2.assistants.models.response.ListMessageResponse;
import com.ren.dianav2.assistants.models.response.MessageResponse;
import com.ren.dianav2.assistants.models.response.RunResponse;
import com.ren.dianav2.assistants.models.response.RunStatusResponse;
import com.ren.dianav2.assistants.models.response.ThreadResponse;
import com.ren.dianav2.listener.IAssistantResponse;
import com.ren.dianav2.listener.IHttpRequest;
import com.ren.dianav2.listener.IImageResponse;
import com.ren.dianav2.listener.IListAssistantResponse;
import com.ren.dianav2.listener.IListMessageResponse;
import com.ren.dianav2.listener.IMessageResponse;
import com.ren.dianav2.listener.IRunResponse;
import com.ren.dianav2.listener.IRunStatusResponse;
import com.ren.dianav2.listener.IThreadResponse;
import com.ren.dianav2.models.Message;
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

    //Assistants
    public void createAssistant(String beta, AssistantRequest assistantRequest, final IAssistantResponse assistantResponse) {
        IHttpRequest request = retrofit.create(IHttpRequest.class);
        Call<AssistantResponse> call = request.createAssistant(context.getString(R.string.api_key),
                beta, assistantRequest);
        call.enqueue(new Callback<AssistantResponse>() {
            @Override
            public void onResponse(Call<AssistantResponse> call, Response<AssistantResponse> response) {
                if (!response.isSuccessful()) {
                    assistantResponse.didError(response.message());
                    return;
                }
                assistantResponse.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<AssistantResponse> call, Throwable throwable) {
                assistantResponse.didError(throwable.getMessage());
            }
        });
    }

    public void getListAssistant(String beta, IListAssistantResponse iListAssistantResponse) {
        IHttpRequest request = retrofit.create(IHttpRequest.class);
        Call<ListAssistantResponse> call = request.getListAssistant(context.getString(R.string.api_key),
                beta);
        call.enqueue(new Callback<ListAssistantResponse>() {
            @Override
            public void onResponse(Call<ListAssistantResponse> call, Response<ListAssistantResponse> response) {
                if (!response.isSuccessful()) {
                    iListAssistantResponse.didError(response.message());
                    return;
                }
                iListAssistantResponse.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ListAssistantResponse> call, Throwable throwable) {
                iListAssistantResponse.didError(throwable.getMessage());
            }
        });
    }

    public void createThread(String beta, ThreadRequest threadRequest, final IThreadResponse iThreadResponse) {
        IHttpRequest request = retrofit.create(IHttpRequest.class);
        Call<ThreadResponse> call = request.createThread(context.getString(R.string.api_key),
                beta, threadRequest);
        call.enqueue(new Callback<ThreadResponse>() {
            @Override
            public void onResponse(Call<ThreadResponse> call, Response<ThreadResponse> response) {
                if (!response.isSuccessful()) {
                    iThreadResponse.didError(response.message());
                    return;
                }
                iThreadResponse.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ThreadResponse> call, Throwable throwable) {
                iThreadResponse.didError(throwable.getMessage());
            }
        });
    }

    public void createMessage(String beta, String threadId, MessageRequest message,
                              final IMessageResponse iMessageResponse) {
        IHttpRequest request = retrofit.create(IHttpRequest.class);
        Call<MessageResponse> call = request.createMessage(context.getString(R.string.api_key),
                beta, threadId, message);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (!response.isSuccessful()) {
                    iMessageResponse.didError(response.message());
                    return;
                }
                iMessageResponse.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
                iMessageResponse.didError(throwable.getMessage());
            }
        });
    }

    public void createRun (String beta, String threadId, RunRequest runRequest, final IRunResponse iRunResponse) {
        IHttpRequest request = retrofit.create(IHttpRequest.class);
        Call<RunResponse> call = request.createRun(context.getString(R.string.api_key),
                beta, threadId, runRequest);
        call.enqueue(new Callback<RunResponse>() {
            @Override
            public void onResponse(Call<RunResponse> call, Response<RunResponse> response) {
                if (!response.isSuccessful()) {
                    iRunResponse.didError(response.message());
                    return;
                }
                iRunResponse.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RunResponse> call, Throwable throwable) {
                iRunResponse.didError(throwable.getMessage());
            }
        });
    }

    //TODO Status not available
    public void getRunStatus(String beta, String threadId, String runId,
                             final IRunStatusResponse iRunStatusResponse) {
        IHttpRequest request = retrofit.create(IHttpRequest.class);
        Call<RunStatusResponse> call = request.getRunStatus(context.getString(R.string.api_key),
                beta, threadId, runId);
        call.enqueue(new Callback<RunStatusResponse>() {
            @Override
            public void onResponse(Call<RunStatusResponse> call, Response<RunStatusResponse> response) {
                if (!response.isSuccessful()) {
                    iRunStatusResponse.didError(response.message());
                    return;
                }
                iRunStatusResponse.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RunStatusResponse> call, Throwable throwable) {
                iRunStatusResponse.didError(throwable.getMessage());
            }
        });
    }

    public void getListMessage(String beta, String threadId, final IListMessageResponse iListMessageResponse) {
        IHttpRequest request = retrofit.create(IHttpRequest.class);
        Call<ListMessageResponse> call = request.getListMessage(context.getString(R.string.api_key),
                beta, threadId);
        call.enqueue(new Callback<ListMessageResponse>() {
            @Override
            public void onResponse(Call<ListMessageResponse> call, Response<ListMessageResponse> response) {
                if (!response.isSuccessful()) {
                    iListMessageResponse.didError(response.message());
                    return;
                }
                iListMessageResponse.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ListMessageResponse> call, Throwable throwable) {
                iListMessageResponse.didError(throwable.getMessage());
            }
        });
    }
}
