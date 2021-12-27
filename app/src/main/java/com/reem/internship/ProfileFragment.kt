package com.reem.internship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import com.reem.internship.databinding.FragmentProfileBinding
import com.reem.internship.databinding.FragmentSignUpBinding


class ProfileFragment : Fragment() {
  lateinit var name : String
  lateinit var email: String
    private var _binding : FragmentProfileBinding? = null
    private val  binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name").toString()
            email = it.getString("email").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name.setText(name)
        binding.email.setText(email)
        binding.filterCity.setOnClickListener { showCityPopupMenu(binding.filterCity) }
        binding.filterMajor.setOnClickListener { showMajorPopupMenu(binding.filterMajor) }
    }

    private fun showCityPopupMenu(view: View) {
        val popup = PopupMenu(this.requireContext(), view)
        popup.inflate(R.menu.city_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {

                R.id.filter_riyadh -> {
                    binding.filterCity.text=getString(R.string.riyadh)
                }
                R.id.filter_dammam -> {
                    binding.filterCity.text=getString(R.string.dammam)
                }
                R.id.filter_jeddah -> {
                    binding.filterCity.text=getString(R.string.jeddah)
                }
                R.id.show_all -> {
                    binding.filterCity.text=getString(R.string.city)
                }



            }

            true
        })

        popup.show()
    }

    private fun showMajorPopupMenu(view: View) {
        val popup = PopupMenu(this.requireContext(), view)
        popup.inflate(R.menu.major_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {

                R.id.filter_is -> {
                    binding.filterMajor.text = getString(R.string.information_systems)
                }
                R.id.filter_cs -> {
                    binding.filterMajor.text = getString(R.string.computer_sciences)
                }
                R.id.filter_se -> {
                    binding.filterMajor.text = getString(R.string.software_engineering)
                }

                R.id.show_all -> {
                    binding.filterMajor.text = getString(R.string.major)
                }

            }

            true
        })

        popup.show()
    }


}