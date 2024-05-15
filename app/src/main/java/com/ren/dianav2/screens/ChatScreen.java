package com.ren.dianav2.screens;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ren.dianav2.R;
import com.ren.dianav2.adapters.MessageAdapter;
import com.ren.dianav2.adapters.ReinforcedMessageAdapter;
import com.ren.dianav2.assistants.models.request.MessageRequest;
import com.ren.dianav2.assistants.models.request.RunRequest;
import com.ren.dianav2.assistants.models.request.ThreadRequest;
import com.ren.dianav2.assistants.models.response.Content;
import com.ren.dianav2.assistants.models.response.ListMessageResponse;
import com.ren.dianav2.assistants.models.response.MessageResponse;
import com.ren.dianav2.assistants.models.response.RunResponse;
import com.ren.dianav2.assistants.models.response.RunStatusResponse;
import com.ren.dianav2.assistants.models.response.Text;
import com.ren.dianav2.assistants.models.response.ThreadResponse;
import com.ren.dianav2.helpers.RequestManager;
import com.ren.dianav2.listener.IListMessageResponse;
import com.ren.dianav2.listener.IMessageResponse;
import com.ren.dianav2.listener.IRunResponse;
import com.ren.dianav2.listener.IRunStatusResponse;
import com.ren.dianav2.listener.IThreadResponse;
import com.ren.dianav2.models.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChatScreen extends AppCompatActivity {

    private TextView welcomeText;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ImageButton micButton;
    private ImageButton microphoneBtn;
    private ImageButton ibBack;
    private ImageButton ibMore;
    private RequestManager requestManager;
    private String idAssistant;
    private List<MessageRequest> messages;
    private RecyclerView rvTextChat;
    private ReinforcedMessageAdapter messageAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String idThread;
    private Dialog mainDialog;
    private Dialog changeNameDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);

        welcomeText = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        micButton = findViewById(R.id.mic_btn);
        microphoneBtn = findViewById(R.id.microphone_icon);

        ibBack = findViewById(R.id.ib_back);
        ibMore = findViewById(R.id.ib_more);

        rvTextChat = findViewById(R.id.rv_chat);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        messages = new ArrayList<>();

        requestManager = new RequestManager(this);

        idAssistant = getIntent().getStringExtra("id");
        //Visibility button
        sendButton.setVisibility(View.GONE);

        //Setup recycler view
        messageAdapter = new ReinforcedMessageAdapter(this, messages, currentUser);
        rvTextChat.setAdapter(messageAdapter);
        rvTextChat.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rvTextChat.setLayoutManager(linearLayoutManager);

        //Init Thread request
        ThreadRequest threadRequest = new ThreadRequest();
        requestManager.createThread("assistants=v2", threadRequest, iThreadResponse);

        onClickBackButton(ibBack);
        onEditTextChange(messageEditText);
        changeStatusBarColor();
        changeNavigationBarColor();
        onSendButtonClick(sendButton);
        onClickMoreButton(ibMore);
        onClickMicButton(micButton);
    }

    //---------------------------- INIT ONCLICK ----------------------------------//
    private void onClickBackButton(ImageButton button) {
        button.setOnClickListener(v -> finish());
    }

    private void onClickMoreButton(ImageButton button) {
        button.setOnClickListener(v -> showDialog());
    }

    private void onClickMicButton(ImageButton button) {
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, VoiceChatScreen.class);
            startActivity(intent);
        });
    }

    //---------------------------- FINISH ONCLICK ----------------------------------//


    //---------------------------- INIT DIALOGS ----------------------------------//
    private void showDialog() {
        mainDialog = new Dialog(this);
        mainDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mainDialog.setContentView(R.layout.bottom_sheet);

        LinearLayout option1 = mainDialog.findViewById(R.id.ll_option_1);
        LinearLayout option2 = mainDialog.findViewById(R.id.ll_option_2);
        LinearLayout option3 = mainDialog.findViewById(R.id.ll_option_3);
        LinearLayout option4 = mainDialog.findViewById(R.id.ll_option_4);

        option1.setOnClickListener(v -> onClickOption(option1));
        option2.setOnClickListener(v -> onClickOption(option2));
        option3.setOnClickListener(v -> onClickOption(option3));
        option4.setOnClickListener(v -> onClickOption(option4));

        mainDialog.setCancelable(true);
        mainDialog.show();
        mainDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mainDialog.getWindow().getAttributes().windowAnimations = R.style.bottom_sheet_animation;
        mainDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void onClickOption(LinearLayout linearLayout) {
        int id = linearLayout.getId();
        if (id == R.id.ll_option_1) {
            showMessage("It doesn't work yet :(");
        } else if (id == R.id.ll_option_2) {
            if (mainDialog != null && mainDialog.isShowing()) {
                mainDialog.dismiss();
            }
            showChangeNameDialog();

        } else if (id == R.id.ll_option_3){
            showMessage("It doesn't work yet :((");
        } else {
            showMessage("Where is your honor trash");
        }
    }

    private void showChangeNameDialog() {
        changeNameDialog = new Dialog(this);
        changeNameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeNameDialog.setContentView(R.layout.bottom_sheet_change_name);
        changeNameDialog.setCancelable(true);
        changeNameDialog.show();
        changeNameDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        changeNameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeNameDialog.getWindow().getAttributes().windowAnimations = R.style.bottom_sheet_animation;
        changeNameDialog.getWindow().setGravity(Gravity.BOTTOM);
        changeNameDialog.setOnDismissListener(dialog -> {
            if (mainDialog != null && !mainDialog.isShowing()) {
                mainDialog.show();
            }
        });
    }

    //---------------------------- FINISH DIALOGS ----------------------------------//

    private void onEditTextChange(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    micButton.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.GONE);
                    microphoneBtn.setVisibility(View.VISIBLE);
                } else {
                    sendButton.setVisibility(View.VISIBLE);
                    micButton.setVisibility(View.GONE);
                    microphoneBtn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void changeStatusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.sky_blue));
        } else {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
        }
    }


    private void changeNavigationBarColor() {
        if (isDarkModeEnabled()) {
            setNavigationBarColor(ContextCompat.getColor(this, R.color.black_variant_1));
        } else {
            setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    private void setNavigationBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().setNavigationBarColor(color);
        }
    }

    private boolean isDarkModeEnabled() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    private void onSendButtonClick(ImageButton sendButton) {
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String question = messageEditText.getText().toString().trim();
        if (!question.isEmpty()) {
            MessageRequest messageRequest = new MessageRequest("user", question);
            messages.add(messageRequest);
            welcomeText.setVisibility(View.GONE);
            messageAdapter.notifyItemInserted(messages.size() - 1);
            rvTextChat.getRecycledViewPool().clear();
            rvTextChat.smoothScrollToPosition(messages.size() - 1);
            requestManager.createMessage("assistants=v2", idThread, messageRequest, iMessageResponse);
            messageEditText.setText("");
        }

    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private final IThreadResponse iThreadResponse = new IThreadResponse() {
        @Override
        public void didFetch(ThreadResponse threadResponse, String msg) {
            idThread = threadResponse.id;
            Log.d("CHAT SCREEN", "Response Thread id: " + idThread);
            showMessage("Thread created with ID: " + idThread);
        }

        @Override
        public void didError(String msg) {
            showMessage("Error: " + msg);
        }
    };

    private final IMessageResponse iMessageResponse = new IMessageResponse() {
        @Override
        public void didFetch(MessageResponse messageResponse, String msg) {
            showMessage("Message added to thread");
            RunRequest runRequest = new RunRequest(idAssistant);
            requestManager.createRun("assistants=v2", idThread, runRequest, iRunResponse);
        }

        @Override
        public void didError(String msg) {
            showMessage("Error creating message: " + msg);
        }
    };

    private final IRunResponse iRunResponse = new IRunResponse() {
        @Override
        public void didFetch(RunResponse runResponse, String msg) {
            requestManager.getRunStatus("assistants=v2", idThread, runResponse.id, iRunStateResponse);
        }

        @Override
        public void didError(String msg) {
            showMessage("Error with run: " + msg);
        }
    };

    private final IRunStatusResponse iRunStateResponse = new IRunStatusResponse() {
        @Override
        public void didFetch(RunStatusResponse runResponse, String msg) {
            if ("completed".equals(runResponse.status)) {
                requestManager.getListMessage("assistants=v2", idThread, iListMessageResponse);
            } else {
                new Handler().postDelayed(() -> requestManager.getRunStatus(
                        "assistants=v2", idThread, runResponse.id, this), 500);
                showMessage(runResponse.status);
                Log.d("CHAT SCREEN", "Run status: " + runResponse.last_error);
            }
        }

        @Override
        public void didError(String msg) {
            showMessage("Error with run status: " + msg);
        }
    };

    private final IListMessageResponse iListMessageResponse = new IListMessageResponse() {
        @Override
        public void didFetch(ListMessageResponse listMessageResponse, String msg) {
            List<MessageResponse> message = listMessageResponse.data.stream()
                    .filter(assistantMessage -> assistantMessage.role.equals("assistant"))
                    .collect(Collectors.toList());
            MessageRequest assistantMessageRequest = new MessageRequest("assistant", message.get(0).content.get(0).text.value);
            messages.add(assistantMessageRequest);
            messageAdapter.notifyItemInserted(messages.size() - 1);
            rvTextChat.smoothScrollToPosition(messages.size() - 1);
            Log.d("CHAT SCREEN", "Assistant message: " + message);
        }

        @Override
        public void didError(String msg) {
            showMessage("Error with list message: " + msg);
        }
    };
}