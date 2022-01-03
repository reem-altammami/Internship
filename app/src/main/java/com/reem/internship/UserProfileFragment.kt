package com.reem.internship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.userProfileFragment =this@UserProfileFragment
        binding.userViewModel = userViewModel
        userViewModel.showProfileDetails()



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
    }

    fun goToEditProfile(){
        findNavController().navigate(R.id.action_userProfileFragment_to_profileFragment)

    }

}