package com.reem.internship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.reem.internship.databinding.FragmentBookMarkBinding
import com.reem.internship.databinding.FragmentEditProfileBinding


class BookMarkFragment : Fragment() {
    private var _binding : FragmentBookMarkBinding? = null
    private val  binding get() = _binding!!
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

    }

    override fun onResume() {
        super.onResume()
     //   (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}