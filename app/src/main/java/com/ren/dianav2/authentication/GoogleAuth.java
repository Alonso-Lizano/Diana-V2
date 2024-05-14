package com.ren.dianav2.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ren.dianav2.R;
import com.ren.dianav2.listener.IGoogleSignInListener;

/***
 * Author: Ren
 */
public class GoogleAuth {
    private static final String TAG = "GoogleAuth";
    public static final int RC_SIGN_IN = 4;

    private Context context;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public GoogleAuth(Context context, FirebaseAuth mAuth, FirebaseUser currentUser) {
        this.context = context;
        configureGoogleSignIn();
        this.mAuth = mAuth;
        this.currentUser = currentUser;
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        ((Activity) context).startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void handleSignInResult(Intent data, IGoogleSignInListener listener) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken(), listener);
        } catch (ApiException e) {
            Log.w(TAG, "Google sign in failed", e);
            listener.onSignInFailed(e.getMessage());
        }
    }

    private void firebaseAuthWithGoogle(String idToken, IGoogleSignInListener listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        currentUser = mAuth.getCurrentUser();
                        listener.onSignInSuccess(currentUser);
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }
}
