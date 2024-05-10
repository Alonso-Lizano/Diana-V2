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

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatViewHolder> {

    private Context context;
    private List<ChatItem> items;

    public RecentChatAdapter(Context context, List<ChatItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecentChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentChatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chats,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentChatViewHolder holder, int position) {
        holder.getIvChat().setImageResource(items.get(position).getIvIcon());
        holder.getTvTitle().setText(items.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class RecentChatViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivChat;
    private TextView tvTitle;
    public RecentChatViewHolder(@NonNull View itemView) {
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
