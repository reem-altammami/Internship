package com.reem.internship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.reem.internship.adapter.TrainingAdapter
import com.reem.internship.databinding.FragmentHomePageBinding
import com.reem.internship.model.CompanyViewModel
import com.reem.internship.model.ViewModelFactory


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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
binding.lifecycleOwner = this
        binding.companyViewModel = viewModel
        binding.trainingRecyclerView.adapter=TrainingAdapter()
        setHasOptionsMenu(true)
//        viewModel.companies.observe(this.viewLifecycleOwner,{
//            binding.status.text = it.toString()
//        })
    }


}