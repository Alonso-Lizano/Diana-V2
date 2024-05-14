package com.ren.dianav2.listener;

import com.google.firebase.auth.FirebaseUser;

public interface IGoogleSignInListener {
    void onSignInSuccess(FirebaseUser user);
    void onSignInFailed(String errorMessage);
}
