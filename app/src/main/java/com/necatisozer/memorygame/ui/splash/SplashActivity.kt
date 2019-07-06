package com.necatisozer.memorygame.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.necatisozer.memorygame.R
import splitties.toast.toast

class SplashActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                response?.error?.localizedMessage?.let { toast(it) }
            }
        }
    }

    private fun signIn() {
        // Choose authentication providers
        val providers = arrayListOf(
            // AuthUI.IdpConfig.PhoneBuilder().build(),
            // AuthUI.IdpConfig.FacebookBuilder().build(),
            // AuthUI.IdpConfig.TwitterBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher)
                .setTheme(R.style.MyTheme_DayNight)
                .build(),
            RC_SIGN_IN
        )
    }


}
