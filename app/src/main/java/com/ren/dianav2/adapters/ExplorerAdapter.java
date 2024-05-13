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
import com.ren.dianav2.assistants.models.response.AssistantData;
import com.ren.dianav2.assistants.models.response.ListAssistantResponse;
import com.ren.dianav2.listener.IAssistantClickListener;
import com.ren.dianav2.models.Item;
import com.ren.dianav2.screens.ChatScreen;
import com.ren.dianav2.screens.ImageChatScreen;

import java.util.List;

public class ExplorerAdapter extends RecyclerView.Adapter<ExplorerViewHolder> {

    private Context context;
    private List<Item> items;
    private Intent intent;
    private List<AssistantData> dataList;
    private IAssistantClickListener clickListener;

    public ExplorerAdapter(Context context, List<Item> items, List<AssistantData> dataList,
                            IAssistantClickListener clickListener) {
        this.context = context;
        this.items = items;
        this.dataList = dataList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ExplorerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExplorerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_explore,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExplorerViewHolder holder, int position) {
        if (position < items.size()) {
            holder.getIvAssistant().setImageResource(items.get(position).getIvAssistant());
            holder.getTvTitle().setText(items.get(position).getTvTitle());
            holder.getTvDescription().setText(items.get(position).getTvDescription());
        } else {
            int dataIndex = position - items.size();
            if (dataIndex < dataList.size()) {
                holder.getIvAssistant().setImageResource(dataList.get(dataIndex).image);
                holder.getTvTitle().setText(dataList.get(dataIndex).name);
                holder.getTvDescription().setText(String.valueOf(dataList.get(dataIndex).description));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            switch (position) {
                case 0:
                    intent = new Intent(context, ChatScreen.class);
                    context.startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(context, ImageChatScreen.class);
                    context.startActivity(intent);
                    break;
                case 2:
                case 3:
                    if (position - items.size() < dataList.size()) {
                        clickListener.onAssistantClicked(dataList.get(position - items.size()).id);
                    } else {
                        Toast.makeText(context, "Assistant data is not available", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size() + (dataList != null ? dataList.size() : 0);
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
