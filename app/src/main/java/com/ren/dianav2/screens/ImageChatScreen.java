package com.ren.dianav2.screens;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ren.dianav2.R;

public class ImageChatScreen extends AppCompatActivity {

    private TextView welcomeText;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ImageButton micButton;
    private ImageButton microphoneBtn;
    private ImageButton ibBack;
    private ImageButton ibMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_chat_screen);

        welcomeText = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        micButton = findViewById(R.id.mic_btn);
        microphoneBtn = findViewById(R.id.microphone_icon);

        ibBack = findViewById(R.id.ib_back);
        ibMore = findViewById(R.id.ib_more);

        //Visibility button
        sendButton.setVisibility(View.GONE);

        onClickBackButton(ibBack);
        onEditTextChange(messageEditText);
        changeStatusBarColor();
        changeNavigationBarColor();
    }

    private void onClickBackButton(ImageButton button) {
        button.setOnClickListener(v -> {
            finish();
        });
    }

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
}