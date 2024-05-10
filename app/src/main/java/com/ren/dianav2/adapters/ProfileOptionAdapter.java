package com.ren.dianav2.adapters;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.dianav2.R;
import com.ren.dianav2.listener.CameraImagePermissionHandler;
import com.ren.dianav2.models.OptionItem;
import com.ren.dianav2.screens.LoginScreen;

import java.util.List;

public class ProfileOptionAdapter extends RecyclerView.Adapter<ProfileOptionViewHolder> {
    private Context context;
    private List<OptionItem> options;
    private CameraImagePermissionHandler permissionHandler;

    public ProfileOptionAdapter(Context context, List<OptionItem> options, CameraImagePermissionHandler permissionHandler) {
        this.context = context;
        this.options = options;
        this.permissionHandler = permissionHandler;
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

        holder.itemView.setOnClickListener(v -> {
            switch (position) {
                case 0:
                    permissionHandler.requestStorageImageAndCameraPermission();
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    Intent intent = new Intent(context, LoginScreen.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    break;
            }
        });
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
