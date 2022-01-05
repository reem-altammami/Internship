package com.reem.internship

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.reem.internship.adapter.BookmarkAdapter
import com.reem.internship.adapter.TrainingAdapter
import com.reem.internship.data.CompanyResponse
import com.reem.internship.model.TrainingApiStatus
import com.reem.internship.ui.BookmarkItemUiState


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,data:List<TrainingItemUiState>?){
    val adapter =recyclerView.adapter as TrainingAdapter
    adapter.submitList(data)
}

@BindingAdapter("data")
fun bindBookmarkRecyclerView(recyclerView: RecyclerView,data:List<BookmarkItemUiState>?){
    val adapter =recyclerView.adapter as BookmarkAdapter
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

@BindingAdapter("trainingApiStatus")

fun bindStatus(statusImageView: ImageView , status:TrainingApiStatus){

    when(status){
        TrainingApiStatus.LOADING ->{
            statusImageView.visibility= View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        TrainingApiStatus.ERROR -> {
            statusImageView.visibility= View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        TrainingApiStatus.EMPTY
                ->{
            statusImageView.visibility= View.VISIBLE
            statusImageView.setImageResource(R.drawable.noresult)

        }
        TrainingApiStatus.DONE ->
            statusImageView.visibility= View.GONE
    }
}

@BindingAdapter("trainingApiStatus")

fun bindStatus(statusAnimationView: LottieAnimationView , status:TrainingApiStatus){

    when(status){
        TrainingApiStatus.LOADING ->{
            statusAnimationView.visibility = View.VISIBLE
            statusAnimationView.setAnimation(R.raw.loading)

        }
        TrainingApiStatus.ERROR -> {
            statusAnimationView.visibility = View.VISIBLE
            statusAnimationView.setAnimation(R.raw.noconnection)
        }
        TrainingApiStatus.EMPTY
        ->{
            statusAnimationView.visibility = View.VISIBLE
            statusAnimationView.setAnimation(R.raw.noresults)

        }
        TrainingApiStatus.DONE ->
            statusAnimationView.visibility= View.GONE
    }
}