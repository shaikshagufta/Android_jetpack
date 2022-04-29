package com.example.mvvmdogs.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mvvmdogs.R

//function to display a spinner when the image is being downloaded
fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 60f
        start()
    }
}
//extension fun for the img view
fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    //we use glide to load "uri"of an image into the the ImageView
    val options= RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.dog)//default img if the img is not downloaded due to some error

    Glide.with(context)
        .setDefaultRequestOptions(options)//options those that we created
        .load(uri)//load the uri
        .into(this)//into this view
}
@BindingAdapter("android:imageUrl")// name used to access the fn
//makes the function accessible to databinding class ie, layout with data binding enabled
fun loadImage(view: ImageView , uri: String?) {
    //using the ext fun we created to load a url into the imageView
    view.loadImage(uri, getProgressDrawable(view.context))
}
