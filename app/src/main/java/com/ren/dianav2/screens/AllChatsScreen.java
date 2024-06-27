package com.ren.dianav2.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ren.dianav2.R;
import com.ren.dianav2.adapters.RecentChatAdapter;
import com.ren.dianav2.adapters.SavedChatAdapter;
import com.ren.dianav2.assistants.models.Conversation;
import com.ren.dianav2.listener.IChatClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AllChatsScreen extends AppCompatActivity {

    private String typeChats;
    private TextView tvTypeChats;
    private ImageButton ibBack;
    private RecyclerView rvExplore;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private RecentChatAdapter recentChatAdapter;
    private SavedChatAdapter savedChatAdapter;

    private ImageView ivProfile;
    private SearchView sv_searchChats;
    private ArrayList<Conversation> conversations = new ArrayList<>();
    private ArrayList<Conversation> conversationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_chats_screen);

        tvTypeChats = findViewById(R.id.tv_typeChat);
        ibBack = findViewById(R.id.ib_back);
        typeChats = getIntent().getStringExtra("Type");
        rvExplore = findViewById(R.id.rv_explore);
        ivProfile = findViewById(R.id.iv_profile);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        sv_searchChats = findViewById(R.id.sv_searchChats);

        setLayoutTitle();
        onClickBackButton(ibBack);
        loadConversations();
        String profile = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : null;
        if (profile != null) {
            Picasso.get().load(profile).into(ivProfile);
        }
        setOnSearchListener();
    }

    private void setLayoutTitle() {
        if (typeChats.equals("Saved")) {
            tvTypeChats.setText(R.string.saved);
        } else {
            tvTypeChats.setText(R.string.recents);
        }
    }

    /**
     * Configura el evento de clic para el botón de retroceso.
     *
     * @param button el botón de retroceso
     */
    private void onClickBackButton(ImageButton button) {
        button.setOnClickListener(v -> finish());
    }

    private void loadConversations() {
        if (typeChats.equals("Saved")) {
            loadSavedConversation();
        } else {
            loadRecentConversation();
        }
    }

    private void loadRecentConversation() {
        db.collection("conversation")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Conversation conversation = snapshot.toObject(Conversation.class);
                        conversationsList.add(conversation);
                    }
                    conversations.clear();
                    conversations.addAll(conversationsList);
                    rvExplore.setHasFixedSize(true);
                    rvExplore.setLayoutManager(new LinearLayoutManager(AllChatsScreen.this,
                            LinearLayoutManager.VERTICAL, false));
                    recentChatAdapter = new RecentChatAdapter(AllChatsScreen.this, conversations, chatClickListener);
                    rvExplore.setAdapter(recentChatAdapter);
                })
                .addOnFailureListener(e -> Log.d("ALL_CHATS_SCREEN", "conversation:onError", e));
    }

    private void loadSavedConversation() {
        db.collection("users")
                .document(currentUser.getUid())
                .collection("favorites")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Conversation conversation = snapshot.toObject(Conversation.class);
                        conversationsList.add(conversation);
                    }
                    conversations.clear();
                    conversations.addAll(conversationsList);
                    rvExplore.setHasFixedSize(true);
                    rvExplore.setLayoutManager(new LinearLayoutManager(AllChatsScreen.this,
                            LinearLayoutManager.VERTICAL, false));
                    savedChatAdapter = new SavedChatAdapter(AllChatsScreen.this, conversations, chatClickListener);
                    rvExplore.setAdapter(savedChatAdapter);
                })
                .addOnFailureListener(e -> Log.e("ALL_CHATS_SCREEN", "Error loading saved chats", e));
    }

    private final IChatClickListener chatClickListener = new IChatClickListener() {
        @Override
        public void onRecentChatClicked(String id) {
            // Implement your logic to handle recent chat click
        }

        @Override
        public void onDeleteChatClicked(String id) {
            // Implement your logic to handle chat deletion
        }
    };

    private String query = "";

    private void setOnSearchListener() {
        sv_searchChats.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for (int i = 0; i < conversationsList.size(); i++) {
                    System.out.println("CONVERSACIONES " + i + ": " + conversationsList.get(i).getTitle());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (query.length() == newText.length()) {
                    conversations = conversationsList;
                } else if (query.trim().length() > newText.trim().length()) {
                    System.out.println("Entraa");
                    ArrayList<Conversation> conversations1 = conversationsList;
                    for (Conversation conver : conversations1) {
                        System.out.println("CONVERSACIONES: " + conver.getTitle());
                        if (conver.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                            if (!conversations.contains(conver)) {
                                System.out.println("AÑADIR: " + conver.getTitle());
                                conversations.add(conver);
                            }
                        }
                    }
                } else {
                    ArrayList<Conversation> conversations1 = conversationsList;
                    for (Conversation conver : conversations1) {
                        if (!conver.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                            System.out.println("CONVERSATION " + conver.getTitle());
                            conversations.remove(conver);
                        } else if (conver.getTitle().contains(newText)) {
                            System.out.println("NO BORRAR: " + conver.getTitle());
                        }
                    }
                }
                query = newText;

                recentChatAdapter = new RecentChatAdapter(AllChatsScreen.this, conversations, chatClickListener);
                rvExplore.setAdapter(recentChatAdapter);
                return true;
            }
        });
    }
}
