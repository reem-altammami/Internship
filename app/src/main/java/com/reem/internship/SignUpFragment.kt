package com.reem.internship

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.reem.internship.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    val TAG=SignUpFragment::class.java.name

private var _binding : FragmentSignUpBinding? = null
    private val  binding get() = _binding!!
    private val signInLauncher =  registerForActivityResult(
    FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    // Choose authentication providers
    val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build())


    // Create and launch sign-in intent
    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var user=FirebaseAuth.getInstance().currentUser
        user?.let{
//            gotoProfileScreen(it)
        }


        binding.signup.setOnClickListener {
            var user=FirebaseAuth.getInstance().currentUser
            val action = SignUpFragmentDirections.actionSignUpFragmentToProfileFragment(email= user?.email!!, name = user.displayName!!)
            findNavController().navigate(action)
            signInLauncher.launch(signInIntent)


        }
    }
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                // Name, email address, and profile photo Url
                val name = user.displayName
                val email = user.email
                Log.d("TAG", "onSignInResult: ${name} ${email}")
            }
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

   fun gotoProfileScreen(user: FirebaseUser){
val action = SignUpFragmentDirections.actionSignUpFragmentToProfileFragment(email= user.email!!, name = user.displayName!!)
       findNavController().navigate(action)

   }

}