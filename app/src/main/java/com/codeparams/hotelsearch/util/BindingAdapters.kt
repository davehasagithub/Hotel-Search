package com.codeparams.hotelsearch.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.codeparams.hotelsearch.R
import com.squareup.picasso.Picasso

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl", "imageDefault")
    fun loadImage(view: ImageView, imageUrl: String?, imageDefault: Drawable?) {
        var img = imageDefault
        if (img == null) {
            img = ContextCompat.getDrawable(view.context, R.color.colorPrimaryDark)
        }

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.with(view.context).isLoggingEnabled = true
            Picasso.with(view.context)
                .load(imageUrl)
                .placeholder(img)
                .error(imageDefault)
                .into(view)
        } else {
            view.setImageDrawable(img)
        }
    }
}
