package com.reem.internship.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reem.internship.BookMarkFragmentDirections
import com.reem.internship.HomePageFragmentDirections
import com.reem.internship.TrainingItemUiState
import com.reem.internship.databinding.BookmarkItemBinding
import com.reem.internship.databinding.TrainingItemBinding
import com.reem.internship.ui.BookmarkItemUiState


class BookmarkAdapter (
    private val onClickListener: (BookmarkItemUiState, Int) -> Unit,
    private val onFavClickListener: (BookmarkItemUiState, Boolean) -> Unit
)  : ListAdapter<BookmarkItemUiState,BookmarkAdapter.BookmarkViewHolder>(DiffCallback){

    class BookmarkViewHolder( private  var binding: BookmarkItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (bookmark : BookmarkItemUiState) {
            binding.bookmark = bookmark
            binding.executePendingBindings()
        }
        val card = binding.trainingCard
        val mark = binding.bookMark
        val unmark = binding.unmark
    }

    companion object DiffCallback: DiffUtil.ItemCallback<BookmarkItemUiState>(){
        override fun areItemsTheSame(
            oldItem: BookmarkItemUiState,
            newItem: BookmarkItemUiState
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: BookmarkItemUiState,
            newItem: BookmarkItemUiState
        ): Boolean {
            return oldItem.image == newItem.image
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkAdapter.BookmarkViewHolder {
        return BookmarkViewHolder(
            BookmarkItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }



    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
val bookmark = getItem(position)
    holder.bind(bookmark)
        holder.mark.visibility = View.VISIBLE
        holder.unmark.visibility = View.GONE
        holder.card.setOnClickListener {


            onClickListener(bookmark, holder.adapterPosition)
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











