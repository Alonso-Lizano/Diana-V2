package com.ren.dianav2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.dianav2.R;
import com.ren.dianav2.models.ChatItem;

import java.util.List;

public class SavedChatAdapter extends RecyclerView.Adapter<SavedChatViewHolder> {

    private Context context;
    private List<ChatItem> chatItems;

    public SavedChatAdapter(Context context, List<ChatItem> chatItems) {
        this.context = context;
        this.chatItems = chatItems;
    }

    @NonNull
    @Override
    public SavedChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedChatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_saved_chat,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedChatViewHolder holder, int position) {
        holder.getTvTitle().setText(chatItems.get(position).getTitle());
        holder.getIvChat().setImageResource(chatItems.get(position).getIvIcon());
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }
}

class SavedChatViewHolder extends RecyclerView.ViewHolder {

    private ImageView ivChat;
    private TextView tvTitle;

    public SavedChatViewHolder(@NonNull View itemView) {
        super(itemView);
        ivChat = itemView.findViewById(R.id.iv_chat);
        tvTitle = itemView.findViewById(R.id.tv_title);
    }

    public ImageView getIvChat() {
        return ivChat;
    }

    public void setIvChat(ImageView ivChat) {
        this.ivChat = ivChat;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }
}
