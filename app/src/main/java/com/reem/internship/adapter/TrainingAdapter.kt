package com.reem.internship.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reem.internship.HomePageFragmentDirections
import com.reem.internship.TrainingItemUiState
import com.reem.internship.data.CompanyResponse
import com.reem.internship.databinding.FragmentHomePageBinding
import com.reem.internship.databinding.TrainingItemBinding

class TrainingAdapter : ListAdapter <TrainingItemUiState,TrainingAdapter.TrainingViewHolder> (DiffCallback){
    var isMark = true

    class TrainingViewHolder( private  var binding: TrainingItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (training : TrainingItemUiState) {
binding.companyTraining = training
            binding.executePendingBindings()
        }
        val card = binding.trainingCard
        val mark = binding.bookMark
        val unmark = binding.unmark
    }




    companion object DiffCallback: DiffUtil.ItemCallback<TrainingItemUiState>(){
        override fun areItemsTheSame(
            oldItem: TrainingItemUiState,
            newItem: TrainingItemUiState
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: TrainingItemUiState,
            newItem: TrainingItemUiState
        ): Boolean {
           return oldItem.image == newItem.image
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
return TrainingViewHolder(TrainingItemBinding.inflate(LayoutInflater.from(parent.context)))    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val training = getItem(position)
        holder.bind(training)
        holder.card.setOnClickListener {
            val action =
                HomePageFragmentDirections.actionHomePageFragmentToTrainingDetailsFragment(position)
            holder.card.findNavController().navigate(action)
        }

        holder.mark.setOnClickListener {   if (isMark) {
            holder.mark.visibility = View.VISIBLE
            isMark = false
            holder.mark.visibility = View.VISIBLE
            holder.unmark.visibility = View.GONE


        } else {
            isMark = true
            holder.unmark.visibility = View.VISIBLE
            holder.mark.visibility = View.GONE
        } }
        holder.unmark.setOnClickListener {
            if (isMark) {
                holder.mark.visibility = View.VISIBLE
                isMark = false
                holder.mark.visibility = View.VISIBLE
                holder.unmark.visibility = View.GONE


            } else {
                isMark = true
                holder.unmark.visibility = View.VISIBLE
                holder.mark.visibility = View.GONE
            }
        }
    }


}
