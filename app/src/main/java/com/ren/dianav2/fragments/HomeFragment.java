package com.ren.dianav2.fragments;

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

import com.ren.dianav2.R;
import com.ren.dianav2.adapters.ExplorerAdapter;
import com.ren.dianav2.adapters.RecentChatAdapter;
import com.ren.dianav2.models.ChatItem;
import com.ren.dianav2.models.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerViewItem;
    private ExplorerAdapter exploreAdapter;
    private RecyclerView recyclerViewChat;
    private RecentChatAdapter recentChatAdapter;
    private List<Item> items;
    private List<ChatItem> chatItems;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewItem = view.findViewById(R.id.rv_explore);
        recyclerViewChat = view.findViewById(R.id.rv_recent_chat);

        recyclerViewItem.setHasFixedSize(true);
        recyclerViewItem.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerViewChat.setHasFixedSize(true);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        addDataToList();

        exploreAdapter = new ExplorerAdapter(getContext(), items);
        recyclerViewItem.setAdapter(exploreAdapter);

        recentChatAdapter = new RecentChatAdapter(getContext(), chatItems);
        recyclerViewChat.setAdapter(recentChatAdapter);

        return view;
    }

    private void changeStatusBarColor() {
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.sky_blue));
        } else {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.blue));
        }
    }

    private void addDataToList() {
        items = new ArrayList<>();
        chatItems = new ArrayList<>();

        items.add(new Item(R.drawable.assistant, "Assistant",
                "Select an assistant to start chatting with"));
        items.add(new Item(R.drawable.round_image_24, "Images", "Search for images"));
        items.add(new Item(R.drawable.round_code_24, "Code", "Search for code"));

        chatItems.add(new ChatItem(R.drawable.round_chat_24, "Java code explanation"));
        chatItems.add(new ChatItem(R.drawable.round_chat_24, "Resolution of mathematical exercises"));
        chatItems.add(new ChatItem(R.drawable.round_chat_24, "Example of how to use your brain"));
    }
}