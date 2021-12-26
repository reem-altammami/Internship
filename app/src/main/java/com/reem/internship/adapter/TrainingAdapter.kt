package com.reem.internship.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reem.internship.data.CompanyResponse
import com.reem.internship.databinding.FragmentHomePageBinding
import com.reem.internship.databinding.TrainingItemBinding

class TrainingAdapter : ListAdapter <CompanyResponse,TrainingAdapter.TrainingViewHolder> (DiffCallback){

    class TrainingViewHolder( private  var binding: TrainingItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (training : CompanyResponse) {
binding.companyTraining = training
            binding.executePendingBindings()
        }
    }




    companion object DiffCallback: DiffUtil.ItemCallback<CompanyResponse>(){
        override fun areItemsTheSame(oldItem: CompanyResponse, newItem: CompanyResponse): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(
            oldItem: CompanyResponse,
            newItem: CompanyResponse
        ): Boolean {
            TODO("Not yet implemented")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
return TrainingViewHolder(TrainingItemBinding.inflate(LayoutInflater.from(parent.context)))    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
val training = getItem(position)
    holder.bind(training)}
}