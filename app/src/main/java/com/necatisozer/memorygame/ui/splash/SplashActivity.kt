package com.necatisozer.memorygame.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.necatisozer.memorygame.R
import com.necatisozer.memorygame.di.injector
import com.necatisozer.memorygame.extension.viewModels
import com.necatisozer.memorygame.ui.base.BaseActivity
import com.necatisozer.memorygame.ui.main.MainActivity
import splitties.activities.start
import splitties.toast.toast

class SplashActivity : BaseActivity() {
    companion object {
        private const val RC_SIGN_IN = 123
    }

    private val viewModel by viewModels { injector.splashViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.openMainEvent().observe(this, Observer { openMain() })
        viewModel.signInEvent().observe(this, Observer { signIn() })
        viewModel.failureEvent().observe(this, Observer { showFailure(it) })
    }

    private fun openMain() {
        start<MainActivity>()
        finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                openMain()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                response?.error?.localizedMessage?.let { toast(it) }
            }
        }
    }
}
