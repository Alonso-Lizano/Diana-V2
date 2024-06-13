package com.ren.dianav2.screens;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ren.dianav2.R;

public class AllChatsScreen extends AppCompatActivity {

    private String typeChats;
    private TextView tvTypeChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_chats_screen);
        tvTypeChats = findViewById(R.id.tv_typeChat);
        typeChats = getIntent().getStringExtra("Type");

        setLayoutTitle();
    }

    private void setLayoutTitle() {
        if (typeChats.equals("Saved")) {
            tvTypeChats.setText(R.string.saved);
        } else {
            tvTypeChats.setText(R.string.recents);
        }
    }
}
