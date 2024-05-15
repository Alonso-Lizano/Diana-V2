package com.ren.dianav2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ren.dianav2.R;
import com.ren.dianav2.assistants.models.request.MessageRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReinforcedMessageAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<MessageRequest> messages;
    private FirebaseUser currentUser;

    public ReinforcedMessageAdapter(Context context, List<MessageRequest> messages, FirebaseUser currentUser) {
        this.context = context;
        this.messages = messages;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (this.getItemViewType(viewType) == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_right_chat,
                    parent, false);
            return new RightMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_left_chat,
                    parent, false);
            return new LeftMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageRequest messageRequest = messages.get(position);
        if (this.getItemViewType(position) == 0) {
            RightMessageViewHolder rightMessageViewHolder = (RightMessageViewHolder) holder;
            rightMessageViewHolder.tvShowMessage.setText(messageRequest.content);
            if (currentUser != null) {
                Picasso.get().load(currentUser.getPhotoUrl()).into(rightMessageViewHolder.ivUser);
            }
        } else {
            LeftMessageViewHolder leftMessageViewHolder = (LeftMessageViewHolder) holder;
            leftMessageViewHolder.tvShowMessage.setVisibility(View.VISIBLE);
            leftMessageViewHolder.tvShowMessage.setText(messageRequest.content);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageRequest messageRequest = messages.get(position);
        return messageRequest.role.equals("user") ? 0 : 1;
    }
}
