package com.ren.dianav2.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ren.dianav2.R;
import com.ren.dianav2.adapters.RecentChatAdapter;
import com.ren.dianav2.adapters.SavedChatAdapter;
import com.ren.dianav2.assistants.models.Conversation;
//import com.ren.dianav2.database.ImageDatabaseManager;
import com.ren.dianav2.listener.IChatClickListener;
import com.ren.dianav2.models.ChatItem;
import com.ren.dianav2.screens.AllChatsScreen;
import com.ren.dianav2.screens.ChatScreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Un {@link Fragment} simple que muestra una lista de chats recientes y guardados.
 * Utiliza el método de construccion {@link ChatFragment#newInstance} para crear una instancia de este fragmento.
 */
public class ChatFragment extends Fragment {

    // parámetros de inicialización del fragmento, p. ej., ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Renombrar y cambiar tipos de parámetros
    private String mParam1;
    private String mParam2;
    //private ImageDatabaseManager miManager;
    private RecyclerView recyclerViewChat;
    private RecentChatAdapter recentChatAdapter;
    private RecyclerView recyclerViewSaved;
    private SavedChatAdapter savedChatAdapter;
    private List<ChatItem> chatItems;
    private List<ChatItem> savedChatItems;
    private Button btnNewChat;
    private Button btnSearchChat;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private ImageView ivProfile;
    private TextView tvSeeAllSaved;
    private TextView tvSeeAllRecent;
    private EditText etSearchChats;
    private ArrayList<Conversation> conversationsRecent = new ArrayList<>();
    private ArrayList<Conversation> conversationsSaved = new ArrayList<>();
    private ArrayList<Conversation> conversationsListR = new ArrayList<>();
    private ArrayList<Conversation> conversationsListS = new ArrayList<>();

    public ChatFragment() {
        // Constructor público requerido
    }

    /**
     * Utiliza este método de construccion para crear una nueva instancia de
     * este fragmento utilizando los parámetros proporcionados.
     *
     * @param param1 Parámetro 1.
     * @param param2 Parámetro 2.
     * @return Una nueva instancia del fragmento ChatFragment.
     */
    // TODO: Renombrar y cambiar tipos y número de parámetros
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // carga el diseño para este fragmento
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerViewChat = view.findViewById(R.id.rv_recent);
        recyclerViewSaved = view.findViewById(R.id.rv_saved);
        btnNewChat = view.findViewById(R.id.btn_newChat);
        btnSearchChat = view.findViewById(R.id.btn_searchChat);
        etSearchChats = view.findViewById(R.id.et_searchChats);
        ivProfile = view.findViewById(R.id.iv_profile);

        tvSeeAllSaved = view.findViewById(R.id.tv_see_all);
        tvSeeAllRecent = view.findViewById(R.id.tv_see_all_recent);

        recyclerViewChat.setHasFixedSize(true);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        loadRecentConversation();
        loadSavedConversation();
        setButtonListeners(view);

        String profile = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : null;
        if (profile != null) {
            Picasso.get().load(profile).into(ivProfile);
        }

        setOnClickAllChats();
        setOnSearchListener();

        return view;
    }

    /**
     * Agrega datos a las listas de chats recientes y guardados.
     */
    private void addDataToList() {
        chatItems = new ArrayList<>();
        savedChatItems = new ArrayList<>();

        chatItems.add(new ChatItem(R.drawable.round_chat_24, getString(R.string.java_code_explanation)));
        chatItems.add(new ChatItem(R.drawable.round_chat_24, getString(R.string.math_exercises_resolution)));
        chatItems.add(new ChatItem(R.drawable.round_chat_24, getString(R.string.use_your_brain_example)));

        savedChatItems.add(new ChatItem(R.drawable.save_icon, getString(R.string.java_code_explanation)));
        savedChatItems.add(new ChatItem(R.drawable.save_icon, getString(R.string.math_exercises_resolution)));
        savedChatItems.add(new ChatItem(R.drawable.save_icon, getString(R.string.use_your_brain_example)));
    }

    /**
     * Configura los listeners de los botones.
     *
     * @param view la vista del fragmento
     */
    private void setButtonListeners(View view) {
        Button btnNewChat = view.findViewById(R.id.btn_newChat);
        Button btnSearchChat = view.findViewById(R.id.btn_searchChat);

        btnNewChat.setOnClickListener(v -> onClickButton(btnNewChat));
        btnSearchChat.setOnClickListener(v -> onClickButton(btnSearchChat));
    }

    /**
     * Maneja los clics de los botones.
     *
     * @param button el botón que fue clicado
     */
    private void onClickButton(Button button) {
        if (button.getId() == R.id.btn_newChat) {
            Intent intent = new Intent(getContext(), ChatScreen.class);
            intent.putExtra("Origin", "NewChat");
            startActivity(intent);
        } else if (button.getId() == R.id.btn_searchChat) {
            btnNewChat.setVisibility(View.GONE);
            btnSearchChat.setVisibility(View.GONE);
            etSearchChats.setVisibility(View.VISIBLE);
        }
    }

    private void loadRecentConversation() {
        db.collection("conversation")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Conversation conversation = snapshot.toObject(Conversation.class);
                        conversationsListR.add(conversation);
                    }
                    conversationsRecent.clear();
                    conversationsRecent.addAll(conversationsListR);
                    recyclerViewChat.setHasFixedSize(true);
                    recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext(),
                            LinearLayoutManager.VERTICAL, false));
                    recentChatAdapter = new RecentChatAdapter(getContext(), conversationsRecent, chatClickListener);
                    recyclerViewChat.setAdapter(recentChatAdapter);
                })
                .addOnFailureListener(e -> Log.d("HOME FRAGMENT", "conversation:onError", e));
    }

    private void loadSavedConversation() {
        db.collection("users")
                .document(currentUser.getUid())
                .collection("favorites")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Conversation conversation = snapshot.toObject(Conversation.class);
                        conversationsListS.add(conversation);
                    }
                    conversationsSaved.clear();
                    conversationsSaved.addAll(conversationsListS);
                    recyclerViewSaved.setHasFixedSize(true);
                    recyclerViewSaved.setLayoutManager(new LinearLayoutManager(requireContext(),
                            LinearLayoutManager.VERTICAL, false));
                    savedChatAdapter = new SavedChatAdapter(requireContext(), conversationsSaved, chatClickListener);
                    recyclerViewSaved.setAdapter(savedChatAdapter);
                })
                .addOnFailureListener(e -> Log.e("ChatFragment", "Error loading saved chats", e));
    }

    private final IChatClickListener chatClickListener = new IChatClickListener() {
        @Override
        public void onRecentChatClicked(String id) {
            Intent intent = new Intent(ChatFragment.this.getContext(), ChatScreen.class);
            intent.putExtra("Origin", "ExistingChat");
            intent.putExtra("IdThread", id);
            startActivity(intent);
        }

        @Override
        public void onDeleteChatClicked(String id) {
            //TODO Not implemented yet
        }
    };

    private void setOnClickAllChats() {
        tvSeeAllSaved.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AllChatsScreen.class);
            intent.putExtra("Type", "Saved");
            startActivity(intent);
        });
        tvSeeAllRecent.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AllChatsScreen.class);
            intent.putExtra("Type", "Recent");
            startActivity(intent);
        });
    }

    private String query = "";

    private void setOnSearchListener() {
        etSearchChats.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkChatExists(s.toString(), conversationsRecent, conversationsListR, "Recent");
                checkChatExists(s.toString(), conversationsSaved, conversationsListS, "Saved");

                recentChatAdapter = new RecentChatAdapter(ChatFragment.this.getContext(), conversationsRecent, chatClickListener);
                recyclerViewChat.setAdapter(recentChatAdapter);

                savedChatAdapter = new SavedChatAdapter(ChatFragment.this.getContext(), conversationsSaved, chatClickListener);
                recyclerViewSaved.setAdapter(savedChatAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            private void checkChatExists(String inputText, ArrayList<Conversation> conversationsVisible, ArrayList<Conversation> conversationsComparable, String type) {

                if (query.length() == inputText.length()) {
                    conversationsVisible = conversationsComparable;
                } else if (query.trim().length() > inputText.trim().length()) {
                    System.out.println("Entraa");
                    ArrayList<Conversation> conversations1 = conversationsVisible;
                    for (Conversation conver : conversations1) {
                        System.out.println("CONVERSACIONES: " + conver.getTitle());
                        if (conver.getTitle().toLowerCase().contains(inputText.toLowerCase())) {
                            if (!conversationsVisible.contains(conver)) {
                                System.out.println("AÑADIR: " + conver.getTitle());
                                conversationsVisible.add(conver);
                            }
                        }
                    }
                } else {
                    ArrayList<Conversation> conversations1 = conversationsComparable;
                    for (Conversation conver : conversations1) {
                        if (!conver.getTitle().toLowerCase().contains(inputText.toLowerCase())) {
                            System.out.println("BORRAR " + conver.getTitle());
                            conversationsVisible.remove(conver);
                        } else if (conver.getTitle().toLowerCase().contains(inputText.toLowerCase())) {
                            System.out.println("NO BORRAR: " + conver.getTitle());
                        }
                    }
                }
                query = inputText;

                if(type.equals("Recent")){
                    System.out.println("ENTRA RECENT:=)");
                    conversationsRecent = conversationsVisible;
                } else {
                    System.out.println("ENTRA SAVEDDDD:=)");
                    conversationsSaved = conversationsVisible;
                }
            }
        });

    }
}
