package com.reem.internship.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.reem.internship.MainActivity
import com.reem.internship.R
import com.reem.internship.dataLayer.UserDataSource
import com.reem.internship.dataLayer.UserRemoteDataSource
import com.reem.internship.model.User
import com.reem.internship.network.CompanyApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var button = findViewById<Button>(R.id.signup)

        button.setOnClickListener {

            // Create and launch sign-in intent
            signInLauncher.launch(signInIntent)
            // [END auth_fui_create_intent] }

        }
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

        val response = result.idpResponse
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid ?: ""
            Log.d("userId", "userId: ${userId}")

//             startActivity(Intent(this,MainActivity::class.java))

            val intent = Intent(this@SignUpActivity, MainActivity::class.java)

            lifecycleScope.async {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    UserRemoteDataSource(CompanyApi.retrofitService).getUserData(userId).collect {
                        Log.d("TAG", "onSignInResult: ${it.toString()}")
                        if (it.id.isEmpty()) {
                            intent.putExtra("isUser", false)
                        } else {
                            //open another activity to display home fragment
                            intent.putExtra("isUser", true)

                        }
                        startActivity(intent)
                    }
                }
            }


        } else {
            if (response == null) {
                finish()
            }
            if (response?.error?.errorCode == ErrorCodes.NO_NETWORK) {
                Toast.makeText(this, response?.error?.errorCode.toString(), Toast.LENGTH_LONG)
                    .show()
                return
            }
            if (response?.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                Toast.makeText(this, response?.error?.errorCode.toString(), Toast.LENGTH_LONG)
                    .show()
                return
            }

            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}