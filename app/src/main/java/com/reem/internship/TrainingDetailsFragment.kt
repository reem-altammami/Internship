package com.reem.internship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.reem.internship.databinding.FragmentTrainingDetailsBinding
import com.reem.internship.model.CompanyViewModel
import com.reem.internship.model.ViewModelFactory


class TrainingDetailsFragment : Fragment() {
    var isMark = true
    var trainingId = 0
    var source = 0
    private var _binding: FragmentTrainingDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CompanyViewModel by activityViewModels { ViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trainingId = it.getInt("id")
            source = it.getInt("index")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTrainingDetailsBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener() {
            findNavController().navigate(TrainingDetailsFragmentDirections.actionTrainingDetailsFragmentToHomePageFragment())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.companyViewModel = viewModel



        getDetails(trainingId, source)

        binding.unmark.setOnClickListener { markTraining() }
        binding.bookmark.setOnClickListener { unMarkTraining() }
    }

    override fun onResume() {
        super.onResume()
        //  (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    fun bookMarkTraining() {
        if (isMark) {
            binding.bookmark.visibility = View.VISIBLE
            isMark = false
            binding.bookmark.visibility = View.VISIBLE
            binding.unmark.visibility = View.GONE


        } else {
            isMark = true
            binding.unmark.visibility = View.VISIBLE
            binding.bookmark.visibility = View.GONE
        }
    }

    fun markTraining() {
        binding.bookmark.visibility = View.VISIBLE
        binding.unmark.visibility = View.GONE
        viewModel.addBooKmark()
    }

    fun unMarkTraining(){
        binding.unmark.visibility = View.VISIBLE
        binding.bookmark.visibility = View.GONE
        viewModel.unBookMarkTraining()
    }

    fun getDetails(id: Int, source: Int) {
        if (source == 0) {

            viewModel.getTrainingDetails(id)
            bindTrainingDetails()

        } else if (source==1) {

            viewModel.getBookmarkDetails(id)
            bindBookmarkDetails()
        }
    }

    fun bindTrainingDetails() {
        viewModel.trainingDetails.observe(this.viewLifecycleOwner, {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it.field
        })
        bindImage(binding.companyImage, viewModel.trainingDetails.value?.image)
        binding.apply {
            city.text = viewModel.trainingDetails.value?.city
            companyName.text = viewModel.trainingDetails.value?.name
            major.text = viewModel.trainingDetails.value?.major
            description.text = viewModel.trainingDetails.value?.description
            companyInfo.text = viewModel.trainingDetails.value?.info
            alertMajor.text = viewModel.trainingDetails.value?.major
            cityAlert.text = viewModel.trainingDetails.value?.city
        }
    }

    fun bindBookmarkDetails() {
        viewModel.bookmarkDetails.observe(this.viewLifecycleOwner, {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it.field
        })
        bindImage(binding.companyImage, viewModel.bookmarkDetails.value?.image)
        binding.apply {
            city.text = viewModel.bookmarkDetails.value?.city
            companyName.text = viewModel.bookmarkDetails.value?.name
            major.text = viewModel.bookmarkDetails.value?.major
            description.text = viewModel.bookmarkDetails.value?.description
            companyInfo.text = viewModel.bookmarkDetails.value?.info
            alertMajor.text = viewModel.bookmarkDetails.value?.major
            cityAlert.text = viewModel.bookmarkDetails.value?.city
        }
    }
}

