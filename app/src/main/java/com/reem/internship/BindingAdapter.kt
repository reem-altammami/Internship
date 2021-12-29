package com.reem.internship

import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.reem.internship.adapter.TrainingAdapter
import com.reem.internship.data.CompanyResponse


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,data:List<TrainingItemUiState>?){
    val adapter =recyclerView.adapter as TrainingAdapter
    adapter.submitList(data)
}
@BindingAdapter("imageUrl")
fun bindImage(imgView:ImageView , imgUrl:String?) {

    imgUrl?.let {
//        val imageLoader = ImageLoader.Builder(imgView.context)
//            .componentRegistry { add(SvgDecoder) }

        val imgUri = imgUrl.toUri().buildUpon().build()
        Log.e("TAG", "uri:${imgUri}")
        Glide.with(imgView)

            .load("https://firebasestorage.googleapis.com/v0/b/test-4d9c6.appspot.com/o/${imgUri}")
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imgView)
        Log.e("TAG", "uri:${imgUri}")
    }
}