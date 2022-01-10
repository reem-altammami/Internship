package com.reem.internship.adapter

import android.util.Log
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
import com.reem.internship.generated.callback.OnClickListener

class TrainingAdapter(
    private val onClickListener: (TrainingItemUiState, Int) -> Unit,
    private val onFavClickListener: (TrainingItemUiState, Boolean) -> Unit
) :
    ListAdapter<TrainingItemUiState, TrainingAdapter.TrainingViewHolder>(DiffCallback) {

    class TrainingViewHolder(private var binding: TrainingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(training: TrainingItemUiState) {
            binding.companyTraining = training
            binding.executePendingBindings()
        }

        val card = binding.trainingCard
        val mark = binding.bookMark
        val unmark = binding.unmark
    }


    companion object DiffCallback : DiffUtil.ItemCallback<TrainingItemUiState>() {
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
        return TrainingViewHolder(TrainingItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {

        val training = getItem(position)
        holder.bind(training)
        holder.card.setOnClickListener {
            onClickListener(training, holder.adapterPosition)
        }

        if (training.isMark) {

            holder.mark.visibility = View.VISIBLE
            holder.unmark.visibility = View.GONE
        } else {
            holder.unmark.visibility = View.VISIBLE
            holder.mark.visibility = View.GONE
        }
        holder.unmark.setOnClickListener {
            onFavClickListener(getItem(holder.adapterPosition), false)

            holder.mark.visibility = View.VISIBLE
            holder.unmark.visibility = View.GONE
        }
        holder.mark.setOnClickListener {
            onFavClickListener(getItem(holder.adapterPosition), true)
            holder.unmark.visibility = View.VISIBLE
            holder.mark.visibility = View.GONE

        }


    }


}
