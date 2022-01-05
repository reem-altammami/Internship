package com.reem.internship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.reem.internship.adapter.BookmarkAdapter
import com.reem.internship.adapter.TrainingAdapter
import com.reem.internship.databinding.FragmentBookMarkBinding
import com.reem.internship.databinding.FragmentEditProfileBinding
import com.reem.internship.model.CompanyViewModel
import com.reem.internship.model.ViewModelFactory
import kotlinx.coroutines.launch


class BookMarkFragment : Fragment() {
    private var _binding : FragmentBookMarkBinding? = null
    private val  binding get() = _binding!!
    private val viewModel: CompanyViewModel by activityViewModels { ViewModelFactory() }

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
        _binding = FragmentBookMarkBinding.inflate(inflater,container,false)
        return binding.root     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.companyViewModel = viewModel
        binding.bookmarkRecyclerView.adapter = BookmarkAdapter()
        setHasOptionsMenu(true)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.bookMarkUiState.collect{
                    bindStatus(binding.statusImage,it.status)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
     //   (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}