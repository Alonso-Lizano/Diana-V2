package com.ren.dianav2.adapters;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
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

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.ren.dianav2.R;
import com.ren.dianav2.listener.IThemeHandler;
import com.ren.dianav2.models.OptionItem;
import com.ren.dianav2.screens.EditProfileScreen;
import com.ren.dianav2.screens.LoginScreen;

import java.util.List;

public class ProfileOptionAdapter extends RecyclerView.Adapter<ProfileOptionViewHolder> {
    private Context context;
    private List<OptionItem> options;
    private IThemeHandler themeHandler;
    private Intent intent;

    public ProfileOptionAdapter(Context context, List<OptionItem> options, IThemeHandler themeHandler) {
        this.context = context;
        this.options = options;
        this.themeHandler = themeHandler;
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
                    intent = new Intent(context, EditProfileScreen.class);
                    context.startActivity(intent);
                    break;
                case 1:
                    Toast.makeText(context, "It doesn't work yet", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    themeHandler.chooseTheme();
                    break;
                case 3:
                    Toast.makeText(context, "It doesn't work yet :(", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(context, "It doesn't work yet :((", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(context, "It doesn't work yet :(((", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    signOut();
                    break;
            }
        });
    }

    public void signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // Facebook sign out
        LoginManager.getInstance().logOut();

        //Google sign out
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        mGoogleSignInClient.signOut();

        Intent intent = new Intent(context, LoginScreen.class);
        context.startActivity(intent);
        ((Activity) context).finish();
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
