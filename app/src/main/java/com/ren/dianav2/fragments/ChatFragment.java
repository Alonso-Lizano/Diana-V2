package com.ren.dianav2.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.ren.dianav2.R;
import com.ren.dianav2.adapters.RecentChatAdapter;
import com.ren.dianav2.adapters.SavedChatAdapter;
import com.ren.dianav2.models.ChatItem;
import com.ren.dianav2.screens.ChatScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerViewChat;
    private RecentChatAdapter recentChatAdapter;
    private RecyclerView recyclerViewSaved;
    private SavedChatAdapter savedChatAdapter;
    private List<ChatItem> chatItems;
    private List<ChatItem> savedChatItems;
    private Button btnOption1;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        changeStatusBarColor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerViewChat = view.findViewById(R.id.rv_recent);
        recyclerViewSaved = view.findViewById(R.id.rv_saved);
        btnOption1 = view.findViewById(R.id.btn_option1);

        recyclerViewChat.setHasFixedSize(true);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        recyclerViewSaved.setHasFixedSize(true);
        recyclerViewSaved.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        addDataToList();

        recentChatAdapter = new RecentChatAdapter(getContext(), chatItems);
        recyclerViewChat.setAdapter(recentChatAdapter);

        savedChatAdapter = new SavedChatAdapter(getContext(), savedChatItems);
        recyclerViewSaved.setAdapter(savedChatAdapter);

        setButtonListeners(view);

        return view;
    }

    private void changeStatusBarColor() {
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.black_variant_1));
        } else {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
    }

    private void addDataToList() {
        chatItems = new ArrayList<>();
        savedChatItems = new ArrayList<>();

        chatItems.add(new ChatItem(R.drawable.round_chat_24, "Java code explanation"));
        chatItems.add(new ChatItem(R.drawable.round_chat_24, "Resolution of mathematical exercises"));
        chatItems.add(new ChatItem(R.drawable.round_chat_24, "Example of how to use your brain"));

        savedChatItems.add(new ChatItem(R.drawable.save_icon, "Java code explanation"));
        savedChatItems.add(new ChatItem(R.drawable.save_icon, "Resolution of mathematical exercises"));
        savedChatItems.add(new ChatItem(R.drawable.save_icon, "Example of how to use your brain"));
    }

    private void setButtonListeners(View view) {
        Button button1 = view.findViewById(R.id.btn_option1);
        Button button2 = view.findViewById(R.id.btn_option2);

        button1.setOnClickListener(v -> onClickButton(button1));
        button2.setOnClickListener(v -> onClickButton(button2));
    }

    private void onClickButton(Button button) {
        if (button.getId() == R.id.btn_option1) {
            Intent intent = new Intent(getContext(), ChatScreen.class);
            startActivity(intent);
        } else if (button.getId() == R.id.btn_option2) {
            Toast.makeText(getContext(), "Search chat", Toast.LENGTH_SHORT).show();
        }
    }
}