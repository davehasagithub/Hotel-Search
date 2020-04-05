package com.codeparams.hotelsearch.ui.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codeparams.hotelsearch.R

internal class HotelDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as LinearLayoutManager?
        if (layoutManager != null) {

            // https://stackoverflow.com/a/49915897
            var position = parent.getChildViewHolder(view).adapterPosition
            if (position == RecyclerView.NO_POSITION) {
                position = parent.getChildViewHolder(view).oldPosition
            }

            if (position != RecyclerView.NO_POSITION) {
                val padding = parent.context.resources.getDimensionPixelSize(R.dimen.margin)

                outRect.left = padding
                outRect.right = padding

                outRect.top = if (position < 1) padding else 0
                outRect.bottom = padding
            }
        }
    }
}
