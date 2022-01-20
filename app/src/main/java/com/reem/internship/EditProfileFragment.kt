package com.reem.internship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.reem.internship.databinding.FragmentEditProfileBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.reem.internship.model.User

import com.reem.internship.model.UserViewModel
import com.reem.internship.model.UserViewModelFactory
import kotlinx.coroutines.launch


class EditProfileFragment : Fragment() {
    //  lateinit var name : String
//  lateinit var email: String
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels { UserViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            name = it.getString("name").toString()
//            email = it.getString("email").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.name.setText(name)
//        binding.email.setText(email)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.profileFragment = this@EditProfileFragment
        binding.userViewModel = userViewModel
        binding.filterCity.setOnClickListener { showCityPopupMenu(binding.filterCity) }
        binding.filterMajor.setOnClickListener { showMajorPopupMenu(binding.filterMajor) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                userViewModel.userUiState.collect {
                    if (it.userItem.userId.isEmpty()) {
                        binding.cancel.visibility = View.GONE
                    } else {
                        binding.cancel.visibility = View.VISIBLE

                        binding.apply {

                            name.setText(it.userItem.userName, TextView.BufferType.SPANNABLE)
                            filterMajor.setText(it.userItem.major, TextView.BufferType.SPANNABLE)
                            email.setText(it.userItem.email, TextView.BufferType.SPANNABLE)
                            filterCity.setText(it.userItem.city, TextView.BufferType.SPANNABLE)
                            gpa.setText(it.userItem.gpa, TextView.BufferType.SPANNABLE)
                            university.setText(
                                it.userItem.university,
                                TextView.BufferType.SPANNABLE
                            )
                        }
                    }
                }
            }
        }
        getProfileImage()


    }

    // City menu
    private fun showCityPopupMenu(view: View) {
        val popup = PopupMenu(this.requireContext(), view)
        popup.inflate(R.menu.city_menu_profile)

        popup.setOnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {

                R.id.filter_riyadh -> {
                    binding.filterCity.text = getString(R.string.riyadh)
                }
                R.id.filter_dammam -> {
                    binding.filterCity.text = getString(R.string.dammam)
                }
                R.id.filter_jeddah -> {
                    binding.filterCity.text = getString(R.string.jeddah)
                }
                R.id.show_all -> {
                    binding.filterCity.text = getString(R.string.city)
                }


            }

            true
        }

        popup.show()
    }

    // Major menu
    private fun showMajorPopupMenu(view: View) {
        val popup = PopupMenu(this.requireContext(), view)
        popup.inflate(R.menu.major_menu_profile)

        popup.setOnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {

                R.id.filter_is -> {
                    binding.filterMajor.text = getString(R.string.information_systems)
                }
                R.id.filter_cs -> {
                    binding.filterMajor.text = getString(R.string.computer_sciences)
                }
                R.id.filter_se -> {
                    binding.filterMajor.text = getString(R.string.software_engineering)
                }


            }

            true
        }

        popup.show()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).bottomNavigation.visibility = View.GONE
    }

    //Check if user already sign up
    fun createNewProfile() {
        val user = addUserInfo()
        if (user.id.isNotEmpty()) {
            userViewModel.addUserToDataBase(user)
            findNavController().navigate(R.id.action_profileFragment_to_userProfileFragment)

        }


    }

    // Navigate to user profile
    fun gotToProfilePage() {
        findNavController().navigate(R.id.action_profileFragment_to_userProfileFragment)

    }

    // Check information is not empty
    fun isUserInfoValid(): Boolean {

        return userViewModel.isEntryValid(
            binding.name.text.toString(),
            binding.email.text.toString(),
            binding.filterMajor.text.toString(),
            binding.filterCity.text.toString(),
            binding.university.text.toString(),
            binding.gpa.text.toString()
        )
    }

    // Save user information as object
    fun addUserInfo(): User {
        var user: User = User()
        if (isUserInfoValid()) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val userName = binding.name.text.toString()
            val email = binding.email.text.toString()
            val major = binding.filterMajor.text.toString()
            val city = binding.filterCity.text.toString()
            val university = binding.university.text.toString()
            val gpa = binding.gpa.text.toString()
            user = User(userName, email, userId, university, major, city, gpa)

        } else {
            setEditTextError()
        }
        return user
    }

    // Load image profile from google
    fun getProfileImage() {
        val image = FirebaseAuth.getInstance().currentUser?.photoUrl
        Glide.with(this)
            .load(image)
            .fitCenter()
            .placeholder(R.drawable.img)
            .into(binding.imageProfile)
    }

    //display error messages when user enter empty fields
    fun setEditTextError() {
        if (binding.name.text!!.isEmpty()) {
            binding.nameLabel.error = getString(R.string.required_field)
        } else {
            binding.nameLabel.error = null
        }
        if (binding.university.text!!.isEmpty()) {
            binding.universityLabel.error = getString(R.string.required_field)
        } else {
            binding.universityLabel.error = null
        }
        if (binding.email.text!!.isEmpty()) {
            binding.emailLabel.error = getString(R.string.required_field)
        } else {
            binding.emailLabel.error = null
        }
        if (binding.gpa.text!!.isEmpty()) {
            binding.gpaLabel.error = getString(R.string.required_field)
        } else {
            binding.gpaLabel.error = null
        }
        if (binding.filterCity.text == "Major") {
            Toast.makeText(requireContext(), getString(R.string.select_major), Toast.LENGTH_SHORT)
                .show()
        }
        if (binding.filterCity.text == "City") {
            Toast.makeText(requireContext(), getString(R.string.select_city), Toast.LENGTH_SHORT)
                .show()
        }

    }

}