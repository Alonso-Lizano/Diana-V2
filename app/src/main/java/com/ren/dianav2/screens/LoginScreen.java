package com.ren.dianav2.screens;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ren.dianav2.R;

import org.json.JSONException;

import java.util.Arrays;

public class LoginScreen extends AppCompatActivity {
    private static final String FACEBOOK_TAG = "FacebookAuth";
    private static final String GOOGLE_TAG = "GoogleAuth";
    private Button buttonEnter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ImageView ivGoogle;
    private ImageView ivFacebook;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

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

        FirebaseApp.initializeApp(this);

        buttonEnter = findViewById(R.id.button_enter);
        ivGoogle = findViewById(R.id.iv_google);
        ivFacebook = findViewById(R.id.iv_facebook);

        //Init google
        configureGoogleSignIn();

        mAuth = FirebaseAuth.getInstance();

        //Init facebook
        callbackManager = CallbackManager.Factory.create();

        AppEventsLogger.activateApp(this.getApplication());
        changeBottomNavigationBar();

        onClickButtonEnter(buttonEnter);
        onClickGoogle(ivGoogle);
        onClickFacebook(ivFacebook);

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
        imageView.setOnClickListener(v -> signIn());
    }

    private void onClickFacebook(ImageView imageView) {
        imageView.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(FACEBOOK_TAG, "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                    showMessage("Login success");
                }

                @Override
                public void onCancel() {
                    Log.d(FACEBOOK_TAG, "facebook:onCancel");
                    showMessage("Login canceled");
                }

                @Override
                public void onError(@NonNull FacebookException e) {
                    Log.d(FACEBOOK_TAG, "facebook:onError", e);
                    showMessage("Login failed");
                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //---------------------------- INIT GOOGLE SIGN ------------------------------------//
    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(GOOGLE_TAG, "signInWithCredential:success");
                            currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(GOOGLE_TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.d(GOOGLE_TAG, "firebaseAuthWithGoogle:" + account.getId());
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            Log.w(GOOGLE_TAG, "Google sign in failed", e);
                        }
                    }
                }
            }
    );

    //---------------------------- FINISH GOOGLE SIGN ----------------------------------//

    //---------------------------- INIT FACEBOOK SIGN ----------------------------------//

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(FACEBOOK_TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(FACEBOOK_TAG, "signInWithCredential:success");
                        currentUser = mAuth.getCurrentUser();
                        updateUI(currentUser);
                        showMessage("Login success");
                        Log.d("FACEBOOK NAME", "NAME " + currentUser.getDisplayName());
                        Log.d("FACEBOOK PHOTO", "PHOTO " + currentUser.getPhotoUrl().toString());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(FACEBOOK_TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    //---------------------------- FINISH FACEBOOK SIGN ----------------------------------//

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, MainScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            showMessage("Login failed");
        }
    }


    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}