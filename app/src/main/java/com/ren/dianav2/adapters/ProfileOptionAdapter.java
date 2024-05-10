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
import com.ren.dianav2.models.OptionItem;

import java.util.List;

public class ProfileOptionAdapter extends RecyclerView.Adapter<ProfileOptionViewHolder> {

    private Context context;
    private List<OptionItem> options;

    public ProfileOptionAdapter(Context context, List<OptionItem> options) {
        this.context = context;
        this.options = options;
    }

    @NonNull
    @Override
    public ProfileOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileOptionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_profile,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileOptionViewHolder holder, int position) {
        holder.getTvOption().setText(options.get(position).getName());
        holder.getIvArrow().setImageResource(options.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return options.size();
    }
}

class ProfileOptionViewHolder extends RecyclerView.ViewHolder {

    private TextView tvOption;
    private ImageView ivArrow;

    public ProfileOptionViewHolder(@NonNull View itemView) {
        super(itemView);
        tvOption = itemView.findViewById(R.id.tv_option);
        ivArrow = itemView.findViewById(R.id.iv_arrow);
    }

    public TextView getTvOption() {
        return tvOption;
    }

    public void setTvOption(TextView tvOption) {
        this.tvOption = tvOption;
    }

    public ImageView getIvArrow() {
        return ivArrow;
    }

    public void setIvArrow(ImageView ivArrow) {
        this.ivArrow = ivArrow;
    }
}
