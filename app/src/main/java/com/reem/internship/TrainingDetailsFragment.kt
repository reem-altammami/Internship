package com.reem.internship

import android.content.Context
import android.content.Context.MODE_PRIVATE
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
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reem.internship.model.BookMark
//import com.reem.internship.notification.AlertWorker
//import com.reem.internship.notification.sheardPrefrenceList
import com.reem.internship.ui.toBookMark
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val SITTING_PREF: String = "shared preferences"
const val ALERTS_NAME = "alert"

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


        binding.share.setOnClickListener {
            shareTrainingDetails()
        }
        binding.apply.setOnClickListener { showApplyDialog() }
        viewModel.isMarked.observe(viewLifecycleOwner, {
            if (it) {
                binding.bookmark.visibility = View.VISIBLE
                binding.unmark.visibility = View.GONE
            } else {
                binding.unmark.visibility = View.VISIBLE
                binding.bookmark.visibility = View.GONE
            }
        })


        getUserAlertList()

        viewModel.getItemTraingListWithBookMArks()

//        if (sheardPrefrenceList.isNotEmpty()) {
//            scheduleNotification()
//        }

        binding.alert.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                addAlert(binding.cityAlert.text.toString(), binding.alertField.text.toString())
            } else {
                deleteAlert(binding.cityAlert.text.toString(), binding.alertField.text.toString())
            }

        }
    }

    override fun onResume() {
        super.onResume()
        //  (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

// Add training to bookmark list
    fun markTraining(bookMark: BookMark) {
        binding.bookmark.visibility = View.VISIBLE
        binding.unmark.visibility = View.GONE
        viewModel.addBooKmark(bookMark)
        val contextView = binding.bookmark
        Snackbar.make(contextView, "Add intern to Bookmark", Snackbar.LENGTH_SHORT).show()

    }
// Delete training from bookmark list
    fun unMarkTraining(trainingId: String) {
        binding.unmark.visibility = View.VISIBLE
        binding.bookmark.visibility = View.GONE
        viewModel.unBookMarkTraining(trainingId)
        val contextView = binding.unmark
        Snackbar.make(contextView, "Remove intern from Bookmark", Snackbar.LENGTH_SHORT).show()
    }
// Get training details
    fun getDetails(id: Int, source: Int) {
        Log.d("trainingId", "trainingId: ${id}${source}")

        viewModel.getTrainingDetails(id, source)
        bindBookmark()
        viewModel.trainingDetails.observe(viewLifecycleOwner, { item ->
            binding.unmark.setOnClickListener { markTraining(item.toBookMark()) }
            binding.bookmark.setOnClickListener { unMarkTraining(item.id) }
        })

    }

// Share training details
    fun shareTrainingDetails() {
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
// Apply on training by email
    fun applyOnTraining() {
        val email = viewModel.trainingDetails.value?.email.toString()
        val subject = viewModel.trainingDetails.value?.field
        val message =
            "${viewModel.profileDetails.value?.name}\n ${viewModel.profileDetails.value?.university}\n${viewModel.profileDetails.value?.gpa}\n${viewModel.profileDetails.value?.major}"
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
// Dialog notify user will apply on training
    fun showApplyDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("${getString(R.string.apply_to)}${viewModel.trainingDetails.value?.field}")
            .setMessage(getString(R.string.cv))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
            .setPositiveButton(getString(R.string.continu)) { _, _ ->
                applyOnTraining()
            }
            .show()
    }

// Check if training is mark
    fun bindBookmark() {
        val id = viewModel.trainingDetails.value?.id!!
        viewModel.isTrainingBookmarked(id)
        Log.d("trainingId", "training id: ${id}")
    }

    fun resetPerf() {
        val sharedPreferences = requireActivity().getSharedPreferences(SITTING_PREF, MODE_PRIVATE)
        val editor =sharedPreferences.edit()
        editor.putString(ALERTS_NAME,null)
        editor.apply()
    }

    //Add user alert to sharedPreferences
    fun addAlert(city: String, major: String) {
        val userAlertList = getAlertListFromSharedPreference()
        userAlertList.find { it.field == major && it.city == city } ?: userAlertList.add(
            Alerts(
                major,
                city
            )
        )
        val sharedPreferences = requireActivity().getSharedPreferences(SITTING_PREF, MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val json = Gson().toJson(userAlertList)
        editor?.putString(ALERTS_NAME, json)
        editor?.apply()
        Log.e("pref", "${userAlertList}")

    }

// Delete user alert from sharedPreferences
    fun deleteAlert(city: String, major: String) {
        val userAlertList = getAlertListFromSharedPreference()
        userAlertList.removeAll { it.field == major && it.city == city }

        val sharedPreferences = requireActivity().getSharedPreferences(SITTING_PREF, MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val json = Gson().toJson(userAlertList)
        editor?.putString(ALERTS_NAME, json)
        editor?.apply()
        Log.e("pref", "${userAlertList}")

    }

    // Get user alerts from sharedPreferences
    private fun getAlertListFromSharedPreference(): MutableList<Alerts> {
        val sharedPreferences =
            requireActivity().getSharedPreferences(SITTING_PREF, MODE_PRIVATE)
        val json = sharedPreferences?.getString(ALERTS_NAME, null)
        val type = object : TypeToken<ArrayList<Alerts>>() {}.type
        json?.let {
            val alertList: ArrayList<Alerts> = Gson().fromJson(it, type)

              return alertList
             }

        return arrayListOf()
    }

    //get newest training match with user alert
    fun getUserAlertList() {
        val userList = viewModel.uiState.value.trainingItemList
        val prefList = getAlertListFromSharedPreference()
        val newestTrainingList = mutableListOf<Alerts>()
        for (item in userList){
            if (isNewTraining(item.publishDate)){
                newestTrainingList.add(Alerts(item.field,item.city))
            }
        }
        for (newest in newestTrainingList){
            for (prefItem in prefList)
            if (newest.field == prefItem.field && newest.city == prefItem.city){}
        }

//        sheardPrefrenceList.add(prefList)
//        Log.e("pref1", "${sheardPrefrenceList}")
//        return prefList
    }
//Check if training is new
    fun isNewTraining (publishDate:String):Boolean{
        val date = SimpleDateFormat("yyyy-MM-dd").parse(publishDate)
        return date.before(Date())
    }


//    fun scheduleNotification() {
//        val notificationWork = PeriodicWorkRequest.Builder(
//            AlertWorker::class.java,
//            16, TimeUnit.MINUTES
//        )
//            .setInitialDelay(3000, TimeUnit.MILLISECONDS)
//            .build()
//
//        WorkManager.getInstance(requireContext())
//            .enqueueUniquePeriodicWork(
//                "myNotifi",
//                ExistingPeriodicWorkPolicy.REPLACE, notificationWork
//            )
//    }


}

data class Alerts(val field: String, val city: String)

var alertList = mutableListOf<Alerts>()



