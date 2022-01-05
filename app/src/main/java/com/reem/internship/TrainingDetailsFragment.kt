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
    var bookmarkId = 0
    private var _binding: FragmentTrainingDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CompanyViewModel by activityViewModels { ViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trainingId = it.getInt("id")
            bookmarkId = it.getInt("index")
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



        getDetails(trainingId,bookmarkId)
        viewModel.trainingDetails.observe(this.viewLifecycleOwner, {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it.field
        })
        binding.unmark.setOnClickListener {viewModel.addBooKmark() }
//        binding.bookmark.setOnClickListener { viewModel.addBooKmark() }
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

    }

    fun getDetails(id: Int ,index:Int){
        if (index==1){
            viewModel.getTrainingDetails(id)
            bindTrainingDetails()
        } else if (index== 0){
            viewModel.getBookmarkDetails(id)
            bindBookmarkDetails()
        }
    }
    fun bindTrainingDetails(){
        bindImage(binding.companyImage,viewModel.trainingDetails.value?.image)
        binding.apply{

        }
    }

    fun bindBookmarkDetails(){
        binding.apply {

        }
    }
}

