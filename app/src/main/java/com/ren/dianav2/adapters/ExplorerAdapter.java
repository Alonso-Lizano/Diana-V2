package com.ren.dianav2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.dianav2.R;
import com.ren.dianav2.models.Item;
import com.ren.dianav2.screens.ChatScreen;
import com.ren.dianav2.screens.ImageChatScreen;

import java.util.List;

public class ExplorerAdapter extends RecyclerView.Adapter<ExplorerViewHolder> {

    private Context context;
    private List<Item> items;
    private Intent intent;

    public ExplorerAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ExplorerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExplorerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_explore,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExplorerViewHolder holder, int position) {
        holder.getIvAssistant().setImageResource(items.get(position).getIvAssistant());
        holder.getTvTitle().setText(items.get(position).getTvTitle());
        holder.getTvDescription().setText(items.get(position).getTvDescription());
        holder.itemView.setOnClickListener(v -> {
            switch (position) {
                case 0:
                    intent = new Intent(context, ChatScreen.class);
                    context.startActivity(intent);
                    break;
                case 1:
                    Toast.makeText(context, "It doesn't work yet :(", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "It doesn't work yet :((", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    intent = new Intent(context, ImageChatScreen.class);
                    context.startActivity(intent);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class ExplorerViewHolder extends RecyclerView.ViewHolder {

    private ImageView ivAssistant;
    private TextView tvTitle;
    private TextView tvDescription;

    public ExplorerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.ivAssistant = itemView.findViewById(R.id.iv_assistant);
        this.tvTitle = itemView.findViewById(R.id.tv_title);
        this.tvDescription = itemView.findViewById(R.id.tv_description);
    }

    public ImageView getIvAssistant() {
        return ivAssistant;
    }

    public void setIvAssistant(ImageView ivAssistant) {
        this.ivAssistant = ivAssistant;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvDescription() {
        return tvDescription;
    }

    public void setTvDescription(TextView tvDescription) {
        this.tvDescription = tvDescription;
    }
}
