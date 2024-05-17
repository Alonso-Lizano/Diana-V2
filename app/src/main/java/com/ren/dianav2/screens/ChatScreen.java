package com.ren.dianav2.screens;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
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
import androidx.annotation.Nullable;
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
import com.ren.dianav2.models.text.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ren.dianav2.R;
import com.ren.dianav2.adapters.ReinforcedMessageAdapter;
import com.ren.dianav2.assistants.models.request.MessageRequest;
import com.ren.dianav2.assistants.models.request.RunRequest;
import com.ren.dianav2.assistants.models.request.ThreadRequest;
import com.ren.dianav2.assistants.models.response.MessageResponse;
import com.ren.dianav2.assistants.models.response.RunResponse;
import com.ren.dianav2.assistants.models.response.RunStatusResponse;
import com.ren.dianav2.assistants.models.response.ThreadResponse;
import com.ren.dianav2.helpers.RequestManager;
import com.ren.dianav2.listener.IMessageResponse;
import com.ren.dianav2.listener.IRunResponse;
import com.ren.dianav2.listener.IRunStatusResponse;
import com.ren.dianav2.listener.IListMessageResponse;
import com.ren.dianav2.listener.IThreadResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * La actividad principal de la pantalla de chat.
 */
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

        microphoneBtn.setOnClickListener(v -> sendVoiceToText());

        idAssistant = getIntent().getStringExtra("id");
        // Visibilidad del botón
        sendButton.setVisibility(View.GONE);

        // Configuración del RecyclerView
        messageAdapter = new ReinforcedMessageAdapter(this, messages, currentUser);
        rvTextChat.setAdapter(messageAdapter);
        rvTextChat.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rvTextChat.setLayoutManager(linearLayoutManager);

        // Inicializar la solicitud de hilo
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

    //---------------------------- INICIO ONCLICK ----------------------------------//
    /**
     * Configura el evento de clic para el botón de retroceso.
     *
     * @param button el botón de retroceso
     */
    private void onClickBackButton(ImageButton button) {
        button.setOnClickListener(v -> finish());
    }

    /**
     * Configura el evento de clic para el botón "más".
     *
     * @param button el botón "más"
     */
    private void onClickMoreButton(ImageButton button) {
        button.setOnClickListener(v -> showDialog());
    }

    /**
     * Configura el evento de clic para el botón del micrófono.
     *
     * @param button el botón del micrófono
     */
    private void onClickMicButton(ImageButton button) {
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, VoiceChatScreen.class);
            startActivityForResult(intent, 20);
        });
    }

    /**
     * Muestra el diálogo principal.
     */
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

    /**
     * Maneja el evento de clic en una opción del diálogo.
     *
     * @param linearLayout el layout de la opción clicada
     */
    private void onClickOption(LinearLayout linearLayout) {
        int id = linearLayout.getId();
        if (id == R.id.ll_option_1) {
            showMessage("It doesn't work yet :(");
        } else if (id == R.id.ll_option_2) {
            if (mainDialog != null && mainDialog.isShowing()) {
                mainDialog.dismiss();
            }
            showChangeNameDialog();

        } else if (id == R.id.ll_option_3) {
            showMessage("It doesn't work yet :((");
        } else {
            showMessage("Where is your honor trash");
        }
    }

    /**
     * Muestra el diálogo para cambiar el nombre.
     */
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


    /**
     * Configura el evento de cambio de texto en el campo de edición de mensajes.
     *
     * @param editText el campo de edición de mensajes
     */
    private void onEditTextChange(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita implementar
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

    /**
     * Cambia el color de la barra de estado.
     */
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

    /**
     * Cambia el color de la barra de navegación.
     */
    private void changeNavigationBarColor() {
        if (isDarkModeEnabled()) {
            setNavigationBarColor(ContextCompat.getColor(this, R.color.black_variant_1));
        } else {
            setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    /**
     * Establece el color de la barra de navegación.
     *
     * @param color el color a establecer
     */
    private void setNavigationBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().setNavigationBarColor(color);
        }
    }

    /**
     * Comprueba si el modo oscuro está habilitado.
     *
     * @return true si el modo oscuro está habilitado, false de lo contrario
     */
    private boolean isDarkModeEnabled() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    /**
     * Configura el evento de clic para el botón de envío.
     *
     * @param sendButton el botón de envío
     */
    private void onSendButtonClick(ImageButton sendButton) {
        sendButton.setOnClickListener(v -> sendMessage());
    }

    /**
     * Envía un mensaje.
     */
    private void sendMessage() {
        String question = messageEditText.getText().toString().trim();
        if (!question.isEmpty()) {
            MessageRequest messageRequest = new MessageRequest("user", question);
            messages.add(messageRequest);

            MessageRequest typingMessage = new MessageRequest(Message.TYPE_TYPING, "typing...");
            messages.add(typingMessage);

            welcomeText.setVisibility(View.GONE);
            messageAdapter.notifyItemInserted(messages.size() - 1);
            rvTextChat.getRecycledViewPool().clear();
            rvTextChat.smoothScrollToPosition(messages.size() - 1);
            requestManager.createMessage("assistants=v2", idThread, messageRequest, iMessageResponse);
            messageEditText.setText("");
        }
    }

    /**
     * Muestra un mensaje.
     *
     * @param msg el mensaje a mostrar
     */
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
            removeTypingMessage();
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

    private void sendVoiceToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Tap to stop recording");

        try {
            startActivityForResult(intent, 10);
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }

    private void removeTypingMessage() {
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (Objects.equals(messages.get(i).role, Message.TYPE_TYPING)) {
                messages.remove(i);
                messageAdapter.notifyItemRemoved(i);
                break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10: {
                if (resultCode == RESULT_OK && null != data) {
                    List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    messageEditText.setText(result.get(0));
                }
                break;
            }
            case 20: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> messages2 = data.getStringArrayListExtra("messages");
                    String sender = data.getStringExtra("sender");
                    int i = 1;
                    if (messages2 != null && !messages2.isEmpty()) {
                        for(String message : messages2){
                            if(i%2 != 0){
                                sender = "user";
                            } else {
                                sender = "AI";
                            }
                            MessageRequest messageRequest = new MessageRequest(sender, message);
                            addMessage(messageRequest);
                            i++;
                        }
                    }
                }
                break;
            }
        }
    }

    private void addMessage(MessageRequest message){
        messages.add(message);
        welcomeText.setVisibility(View.GONE);
        messageAdapter.notifyItemInserted(messages.size() - 1);
        rvTextChat.smoothScrollToPosition(messages.size() - 1);
        messageAdapter.notifyDataSetChanged();
    }
}