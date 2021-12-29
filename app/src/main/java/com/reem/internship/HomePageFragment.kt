package com.reem.internship

import android.os.Bundle
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





class HomePageFragment : Fragment() {
private var  _binding : FragmentHomePageBinding?= null
    private val binding get() = _binding!!
    private val viewModel :CompanyViewModel by activityViewModels { ViewModelFactory() }

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
        _binding = FragmentHomePageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
binding.lifecycleOwner = this@HomePageFragment
        binding.companyViewModel = viewModel
        binding.trainingRecyclerView.adapter=TrainingAdapter()
        setHasOptionsMenu(true)
        binding.filterMajor.setOnClickListener { showMajorPopupMenu(binding.filterMajor) }
        binding.filterCity.setOnClickListener { showCityPopupMenu(binding.filterCity) }

//        viewModel.companies.observe(this.viewLifecycleOwner,{
//            binding.status.text = it.toString()
//        })
    }


    private fun showMajorPopupMenu(view: View) {
        val popup = PopupMenu(this.requireContext(), view)
        popup.inflate(R.menu.major_menu)

        popup.setOnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {

                R.id.filter_is -> {
                    binding.filterMajor.text = getString(R.string.information_systems)
                    viewModel.getCompany(getString(R.string.information_systems))
                }
                R.id.filter_cs -> {
                    binding.filterMajor.text = getString(R.string.computer_sciences)
                    viewModel.getCompany(getString(R.string.computer_sciences))

                }
                R.id.filter_se -> {
                    binding.filterMajor.text = getString(R.string.software_engineering)
                    viewModel.getCompany(getString(R.string.software_engineering))

                }

                R.id.show_all -> {
                    binding.filterMajor.text = getString(R.string.major)
                    viewModel.getCompany("")
                }

            }

            true
        }

        popup.show()
    }

    private fun showCityPopupMenu(view: View) {
        val popup = PopupMenu(this.requireContext(), view)
        popup.inflate(R.menu.city_menu)

        popup.setOnMenuItemClickListener{ item: MenuItem? ->

            when (item!!.itemId) {

                R.id.filter_riyadh -> {
                    binding.filterCity.text=getString(R.string.riyadh)
                    viewModel.getTrainingFilteredByCity(getString(R.string.riyadh))
                }
                R.id.filter_dammam -> {
                    binding.filterCity.text=getString(R.string.dammam)
                    viewModel.getTrainingFilteredByCity(getString(R.string.dammam))

                }
                R.id.filter_jeddah -> {
                    binding.filterCity.text=getString(R.string.jeddah)
                    viewModel.getTrainingFilteredByCity(getString(R.string.jeddah))
                }

                R.id.show_all -> {
                    binding.filterCity.text=getString(R.string.city)
                    viewModel.getCompany("")
                }

            }

            true
        }

        popup.show()
    }

    override fun onResume() {
        super.onResume()
      //  (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}