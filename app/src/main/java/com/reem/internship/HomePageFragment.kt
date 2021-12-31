package com.reem.internship

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import com.reem.internship.adapter.TrainingAdapter
import com.reem.internship.databinding.FragmentHomePageBinding
import com.reem.internship.model.CompanyViewModel
import com.reem.internship.model.ViewModelFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.reem.internship.model.User
import com.reem.internship.network.CompanyApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomePageFragment : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CompanyViewModel by activityViewModels { ViewModelFactory() }
    private var currentCity = ""
    private var currentMajor = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this@HomePageFragment
        binding.companyViewModel = viewModel
        binding.trainingRecyclerView.adapter = TrainingAdapter()
        setHasOptionsMenu(true)
        binding.filterMajor.setOnClickListener { showMajorPopupMenu(binding.filterMajor) }
        binding.filterCity.setOnClickListener { showCityPopupMenu(binding.filterCity) }


        var id = FirebaseAuth.getInstance().currentUser?.uid
        var username = FirebaseAuth.getInstance().currentUser?.displayName
        var email = FirebaseAuth.getInstance().currentUser?.email
FirebaseAuth.getInstance().currentUser?.uid
        GlobalScope.launch {
            var s = CompanyApi.retrofitService.pustUserData(id!!, User("dfe","fergr",id)!!)

                Log.d("fffff", "onViewCreated: ${s.toString()} ")

        }
//        viewModel.companies.observe(this.viewLifecycleOwner,{
//            binding.status.text = it.toString()
//        })
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect {
                    bindStatus(binding.statusImage, it.status)
                }
            }
        }
    }


    private fun showMajorPopupMenu(view: View) {
        val popup = PopupMenu(this.requireContext(), view)
        popup.inflate(R.menu.major_menu)

        popup.setOnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {

                R.id.filter_is -> {
                    currentMajor = getString(R.string.information_systems)

                }
                R.id.filter_cs -> {
                    currentMajor = getString(R.string.computer_sciences)

                }
                R.id.filter_se -> {
                    currentMajor = getString(R.string.software_engineering)


                }

                R.id.show_all -> {
                    currentMajor = ""
                }

            }
            if (currentMajor.isNotEmpty()) {
                binding.filterMajor.text = currentMajor
            } else {
                binding.filterMajor.text = getString(R.string.major)
            }
            viewModel.getCompany(major = currentMajor, city = currentCity)

            true
        }

        popup.show()
    }

    private fun showCityPopupMenu(view: View) {
        val popup = PopupMenu(this.requireContext(), view)
        popup.inflate(R.menu.city_menu)

        popup.setOnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {

                R.id.filter_riyadh -> {
                    currentCity = getString(R.string.riyadh)
                }
                R.id.filter_dammam -> {
                    currentCity = getString(R.string.dammam)

                }
                R.id.filter_jeddah -> {
                    currentCity = getString(R.string.jeddah)
                }

                R.id.show_all -> {
                    currentCity = ""
                }

            }
            if (currentCity.isNotEmpty()) {
                binding.filterCity.text = currentCity
            } else {
                binding.filterCity.text = getString(R.string.city)
            }
            viewModel.getCompany(major = currentMajor, city = currentCity)
            true
        }

        popup.show()
    }

    override fun onResume() {
        super.onResume()
        //  (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}