package com.reem.internship

import android.content.Intent
import android.net.Uri
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
import android.content.pm.PackageManager
import android.util.Log
import android.widget.ImageView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class TrainingDetailsFragment : Fragment() {
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
        binding.share.setOnClickListener {
            shareTrainingDetails()
        }
        binding.apply.setOnClickListener { showApplyDialog() }
        viewModel.isMarked.observe(viewLifecycleOwner,{ if (it){
            binding.bookmark.visibility = View.VISIBLE
            binding.unmark.visibility = View.GONE
        } else {
            binding.unmark.visibility = View.VISIBLE
            binding.bookmark.visibility = View.GONE
        }})
    }

    override fun onResume() {
        super.onResume()
        //  (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }



    fun markTraining() {
        binding.bookmark.visibility = View.VISIBLE
        binding.unmark.visibility = View.GONE
        viewModel.addBooKmark()
        val contextView = binding.bookmark
        Snackbar.make(contextView, "Add intern to Bookmark", Snackbar.LENGTH_SHORT).show()

    }

    fun unMarkTraining(){
        binding.unmark.visibility = View.VISIBLE
        binding.bookmark.visibility = View.GONE
        viewModel.unBookMarkTraining()
        val contextView = binding.unmark
        Snackbar.make(contextView, "Remove intern from Bookmark", Snackbar.LENGTH_SHORT).show()
    }

    fun getDetails(id: Int, source: Int){
        Log.d("trainingId", "trainingId: ${id}${source}")

        viewModel.getTrainingDetails(id,source)
        bindBookmark()

//        bindTrainingDetails()
    }

//    fun getDetails(id: Int, source: Int) {
//        if (source == 0) {
//
//            viewModel.getTrainingDetails(id)
//            bindTrainingDetails()
//
//        } else if (source==1) {
//            Log.e("TAG", "getDetails: ${id}", )
//            viewModel.getBookmarkDetails(id)
//            bindBookmarkDetails()
//        }
//    }

    fun bindTrainingDetails() {
        viewModel.trainingDetails.observe(this.viewLifecycleOwner, {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it.field
        })
//bindBookmark(viewModel.trainingDetails.value?.id!!)
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
            bookmark.visibility = View.VISIBLE
            unmark.visibility = View.GONE
        }
    }

    fun shareTrainingDetails(){
        val intent = Intent(Intent.ACTION_SEND)
            .putExtra(
                Intent.EXTRA_TEXT,
                " ${viewModel.trainingDetails.value?.field}\n ${viewModel.trainingDetails.value?.name}\n ${viewModel.trainingDetails.value?.description}"
            )
            .setType("text/plain")
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        }

    }

fun applyOnTraining(){
 val email = viewModel.trainingDetails.value?.email.toString()
    val subject = viewModel.trainingDetails.value?.field
    val message = "${viewModel.profileDetails.value?.name}\n ${viewModel.profileDetails.value?.university}\n${viewModel.profileDetails.value?.gpa}\n${viewModel.profileDetails.value?.major}"
    Log.d("message", "message: ${message}")

    val addresses = email.split(",".toRegex()).toTypedArray()

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // only email apps should handle this
        putExtra(Intent.EXTRA_EMAIL, addresses)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, message)
    }
    val packageManager = requireActivity().packageManager

    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }

}

    fun showApplyDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Apply To ${viewModel.trainingDetails.value?.field}")
            .setMessage("You will apply to this intern \n we recommend you to share your CV on email ")
            .setCancelable(false)
            .setNegativeButton("Cancel"){_,_ ->
            }
            .setPositiveButton("Continue"){_,_->
                applyOnTraining()
            }
            .show()
    }


fun bindBookmark(){
    val id = viewModel.trainingDetails.value?.id!!
    viewModel.isTrainingBookmarked(id)
    Log.d("trainingId", "training id: ${id}")

}

}

