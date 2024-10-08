package com.ren.dianav2.listener;

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
import com.ren.dianav2.models.Message;
import com.ren.dianav2.models.request.images.ImageRequest;
import com.ren.dianav2.models.response.images.ImageResponse;
import com.ren.dianav2.models.speech.SpeechRequest;
import com.ren.dianav2.models.text.TextRequest;
import com.ren.dianav2.models.text.TextResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IHttpRequest {

    @POST("images/generations")
    Call<ImageResponse> generateImage(
            @Header("Authorization") String authorization,
            @Body ImageRequest imageRequest
    );


    //Assistants
    //Create
    @POST("assistants")
    Call<AssistantResponse> createAssistant(
            @Header("Authorization") String authorization,
            @Header("OpenAI-Beta") String beta,
            @Body AssistantRequest assistantRequest
    );

    //List Assistant
    @GET("assistants")
    Call<ListAssistantResponse> getListAssistant(
            @Header("Authorization") String authorization,
            @Header("OpenAI-Beta") String beta
    );

    //Create thread
    @POST("threads")
    Call<ThreadResponse> createThread(
            @Header("Authorization") String authorization,
            @Header("OpenAI-Beta") String beta,
            @Body ThreadRequest threadRequest
    );

    //Create message
    @POST("threads/{thread_id}/messages")
    Call<MessageResponse> createMessage(
            @Header("Authorization") String authorization,
            @Header("OpenAI-Beta") String beta,
            @Path("thread_id") String threadId,
            @Body MessageRequest message
    );

    //Create run
    @POST("threads/{thread_id}/runs")
    Call<RunResponse> createRun(
            @Header("Authorization") String authorization,
            @Header("OpenAI-Beta") String beta,
            @Path("thread_id") String threadId,
            @Body RunRequest runRequest
    );

    //Get run
    @GET("threads/{thread_id}/runs/{run_id}")
    Call<RunStatusResponse> getRunStatus(
            @Header("Authorization") String authorization,
            @Header("OpenAI-Beta") String beta,
            @Path("thread_id") String threadId,
            @Path("run_id") String runId
    );

    //List messages
    @GET("threads/{thread_id}/messages")
    Call<ListMessageResponse> getListMessage(
            @Header("Authorization") String authorization,
            @Header("OpenAI-Beta") String beta,
            @Path("thread_id") String threadId
    );

    @POST("chat/completions")
    Call<TextResponse> generateMessage(
            @Header("Authorization") String authorization,
            @Body TextRequest textRequest
    );

    @Headers({
            "xi-api-key: 69ad3321f4aadd7eb671422814f122d3"
    })
    @POST("text-to-speech/AZnzlk1XvdvUeBnXmlld")
    Call<ResponseBody> generateSpeech(
            @Body SpeechRequest request
    );
}
