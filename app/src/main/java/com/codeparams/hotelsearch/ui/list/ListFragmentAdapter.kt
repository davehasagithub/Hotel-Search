package com.codeparams.hotelsearch.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codeparams.hotelsearch.R
import com.codeparams.hotelsearch.data.HotelDb
import com.codeparams.hotelsearch.databinding.HotelRowBinding

class ListFragmentAdapter internal constructor() :
    PagedListAdapter<HotelDb, ListFragmentAdapter.ViewHolder>(DIFF_CALLBACK),
    DefaultLifecycleObserver {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.hotel_row, viewGroup, false
            )
        )
    }

    inner class ViewHolder(val binding: HotelRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {}
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val hotel = getItem(position)
        if (hotel != null) {
            viewHolder.binding.hotel = hotel
            viewHolder.binding.executePendingBindings()
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?: RecyclerView.NO_ID
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HotelDb>() {
            override fun areItemsTheSame(oldHotel: HotelDb, newHotel: HotelDb): Boolean {
                return oldHotel.id == newHotel.id
            }

            override fun areContentsTheSame(oldHotel: HotelDb, newHotel: HotelDb): Boolean {
                return oldHotel == newHotel
            }
        }
    }
}
