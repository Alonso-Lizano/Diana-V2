package com.ren.dianav2.screens;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ren.dianav2.R;
import com.ren.dianav2.authentication.GoogleAuth;
import com.ren.dianav2.listener.IGoogleSignInListener;

public class LoginScreen extends AppCompatActivity implements IGoogleSignInListener {

    private Button buttonEnter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private GoogleAuth googleAuth;
    private ImageView ivGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonEnter = findViewById(R.id.button_enter);
        ivGoogle = findViewById(R.id.iv_google);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        googleAuth = new GoogleAuth(this, mAuth, currentUser);

        changeBottomNavigationBar();

        onClickButtonEnter(buttonEnter);
        onClickGoogle(ivGoogle);
    }

    private void changeBottomNavigationBar() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black_variant_1));
        }
    }

    private void onClickButtonEnter(Button button) {
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
            finish();
        });
    }

    private void onClickGoogle(ImageView imageView) {
        imageView.setOnClickListener(v -> googleAuth.signIn());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
            finish();
        }
    }

    //---------------------------- INIT GOOGLE SIGN ------------------------------------//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoogleAuth.RC_SIGN_IN) {
            googleAuth.handleSignInResult(data, this);
        }
    }

    //---------------------------- FINISH GOOGLE SIGN ----------------------------------//

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignInSuccess(FirebaseUser user) {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
        showMessage("Sign in success");
    }

    @Override
    public void onSignInFailed(String errorMessage) {
        //TODO nothing to do here
    }
}