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
import com.ren.dianav2.adapters.ProfileOptionAdapter;
import com.ren.dianav2.models.OptionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ProfileOptionAdapter adapter;
    private List<OptionItem> optionItems;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = view.findViewById(R.id.rv_option);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        addDataToList();

        adapter = new ProfileOptionAdapter(getContext(), optionItems);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void addDataToList() {
        optionItems = new ArrayList<>();

        optionItems.add(new OptionItem("Edit photo", R.drawable.round_arrow_forward_ios_24));
        optionItems.add(new OptionItem("Font size", R.drawable.round_arrow_forward_ios_24));
        optionItems.add(new OptionItem("Theme", R.drawable.round_arrow_forward_ios_24));
        optionItems.add(new OptionItem("Notification", R.drawable.round_arrow_forward_ios_24));
        optionItems.add(new OptionItem("Privacy", R.drawable.round_arrow_forward_ios_24));
        optionItems.add(new OptionItem("Add account", R.drawable.round_arrow_forward_ios_24));
        optionItems.add(new OptionItem("Logout", R.drawable.round_arrow_forward_ios_24));
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
}