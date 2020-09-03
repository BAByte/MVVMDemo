package com.ba.ex.mvvmsample.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ba.ex.mvvmsample.R

/**
 * 自定义DataBinding的Adapter
 */
@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.color.colorAccent)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}
