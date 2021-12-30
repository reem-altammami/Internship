package com.reem.internship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.reem.internship.databinding.FragmentProfileBinding
import com.reem.internship.databinding.FragmentTrainingDetailsBinding
import com.reem.internship.model.CompanyViewModel
import com.reem.internship.model.ViewModelFactory


class TrainingDetailsFragment : Fragment() {
    var trainingId = 0
    private var _binding : FragmentTrainingDetailsBinding? = null
    private val  binding get() = _binding!!
    private val viewModel : CompanyViewModel by activityViewModels { ViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
          trainingId = it.getInt("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTrainingDetailsBinding.inflate(inflater,container,false)

//         (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)


        return binding.root     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.companyViewModel = viewModel
        viewModel.getTrainingDetails(trainingId)
        viewModel.trainingDetails.observe(this.viewLifecycleOwner,{
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it.field
        })

    }

    override fun onResume() {
        super.onResume()
      //  (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }



}