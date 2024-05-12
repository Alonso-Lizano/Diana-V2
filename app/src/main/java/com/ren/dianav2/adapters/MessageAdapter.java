package com.ren.dianav2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.dianav2.R;
import com.ren.dianav2.listener.IImageClick;
import com.ren.dianav2.models.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Message> messages;
    private IImageClick iImageClick;

    public MessageAdapter(Context context, List<Message> messages, IImageClick iImageClick) {
        this.context = context;
        this.messages = messages;
        this.iImageClick = iImageClick;
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
        Message message = messages.get(position);
        if (this.getItemViewType(position) == 0) {
            RightMessageViewHolder rightMessageViewHolder = (RightMessageViewHolder) holder;
            rightMessageViewHolder.tvShowMessage.setText(message.getMessage());
        } else {
            LeftMessageViewHolder leftMessageViewHolder = (LeftMessageViewHolder) holder;
            if (message.isImage()) {
                leftMessageViewHolder.llImageCard.setVisibility(View.VISIBLE);
                leftMessageViewHolder.tvShowMessage.setVisibility(View.GONE);
                Picasso.get().load(message.getMessage()).into(leftMessageViewHolder.ivImage);
                leftMessageViewHolder.itemView.setOnClickListener(v -> {
                    iImageClick.onImageClick(messages.get(holder.getAdapterPosition()).getMessage());
                });
            } else {
                leftMessageViewHolder.tvShowMessage.setVisibility(View.VISIBLE);
                leftMessageViewHolder.tvShowMessage.setText(message.getMessage());
            }

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.isUser() ? 0 : 1;
    }
}

class RightMessageViewHolder extends RecyclerView.ViewHolder {

    public TextView tvShowMessage;

    public RightMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        tvShowMessage = itemView.findViewById(R.id.tv_show_message);
    }
}

class LeftMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView tvShowMessage;
    public LinearLayout llImageCard;
    public ImageView ivImage;
    public LeftMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        tvShowMessage = itemView.findViewById(R.id.tv_show_message);
        llImageCard = itemView.findViewById(R.id.ll_image_card);
        ivImage = itemView.findViewById(R.id.iv_image);
    }
}
