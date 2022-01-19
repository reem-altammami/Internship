package com.reem.internship

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.reem.internship.databinding.FragmentHomePageBinding
import com.reem.internship.databinding.FragmentUserProfileBinding
import com.reem.internship.model.UserViewModel
import com.reem.internship.model.UserViewModelFactory
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel : UserViewModel by viewModels{UserViewModelFactory()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.switchtheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
//        binding.light.setOnClickListener {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            binding.dark.visibility = View.VISIBLE
//            binding.light.visibility = View.GONE
//        }
//
//        binding.dark.setOnClickListener {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            binding.light.visibility = View.VISIBLE
//            binding.dark.visibility = View.GONE
//
//        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.userProfileFragment =this@UserProfileFragment
        binding.userViewModel = userViewModel
//        userViewModel.showProfileDetails()
binding.logOut.setOnClickListener {
    FirebaseAuth.getInstance().signOut()
    val intent = Intent(this@UserProfileFragment.requireContext(), SignUpActivity::class.java)
    startActivity(intent)
}


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                userViewModel.userUiState.collect {
                    binding.userName.text=it.userItem.userName
                    binding.major.text=it.userItem.major
                    binding.email.text=it.userItem.email
                    binding.city.text=it.userItem.city
                    binding.gpa.text=it.userItem.gpa
                    binding.university.text=it.userItem.university

                }
            }
        }

        getProfileImage()
    }

    fun goToEditProfile(){
        findNavController().navigate(R.id.action_userProfileFragment_to_profileFragment)

    }

    fun getProfileImage(){
        val image = FirebaseAuth.getInstance().currentUser?.photoUrl
        Glide.with(this)
            .load(image)
            .fitCenter()
            .placeholder(R.drawable.img)
            .into(binding.imageProfile)
    }

    override fun onResume() {
        super.onResume()
        //  (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        (requireActivity() as MainActivity).bottomNavigation.visibility = View.VISIBLE
    }




}