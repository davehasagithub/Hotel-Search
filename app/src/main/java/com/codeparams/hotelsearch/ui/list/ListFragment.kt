package com.codeparams.hotelsearch.ui.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codeparams.hotelsearch.R
import com.codeparams.hotelsearch.data.HotelSortBy
import com.codeparams.hotelsearch.databinding.ListFragmentBinding
import com.codeparams.hotelsearch.ui.MainActivityViewModel
import com.codeparams.hotelsearch.util.UiUtils
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ListFragment : DaggerFragment() {

    private var initialized = false

    private lateinit var binding: ListFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var adapter: ListFragmentAdapter

    private var menu: Menu? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BUNDLE_KEY_INITIALIZED, initialized)
        // TODO remember scroll position if app is killed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        adapter = ListFragmentAdapter()
        adapter.setHasStableIds(true)

        initialized = savedInstanceState?.getBoolean(BUNDLE_KEY_INITIALIZED) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentBinding.inflate(inflater)
        UiUtils.doPostponedTransition(this, binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewHotels.setHasFixedSize(true)
        binding.recyclerviewHotels.adapter = adapter
        binding.recyclerviewHotels.addItemDecoration(HotelDecoration())
        binding.recyclerviewHotels.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_fragment, menu)
        this.menu = menu
        if (!initialized) {
            initialized = true
            mainActivityViewModel.changeSort(mainActivityViewModel.sortBy)
        } else {
            selectSortMenuItem(mainActivityViewModel.sortBy)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val newSort =
            when (item.itemId) {
                R.id.action_sort_by_hotel_name -> HotelSortBy.NAME
                R.id.action_sort_by_price -> HotelSortBy.PRICE
                R.id.action_sort_by_guest_rating -> HotelSortBy.GUEST_RATING
                else -> null
            }

        newSort?.let {
            mainActivityViewModel.changeSort(newSort)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let { activity ->
            mainActivityViewModel = ViewModelProvider(activity, viewModelFactory).get(
                MainActivityViewModel::class.java
            )
        }

        addViewModelObservers()
    }

    private fun addViewModelObservers() {
        mainActivityViewModel.hotelsLiveData?.observe(viewLifecycleOwner, Observer { hotels ->
            adapter.submitList(null) { adapter.submitList(hotels) }
        })

        mainActivityViewModel.sortByLiveData?.observe(viewLifecycleOwner, Observer { sortBy ->
            selectSortMenuItem(sortBy)
            updateSubtitle(sortBy)
        })
    }

    private fun updateSubtitle(sortBy: HotelSortBy) {
        (activity as AppCompatActivity).supportActionBar?.subtitle =
            getString(R.string.subtitle_sort_by, getString(getSortSubtitle(sortBy)))
    }

    private fun getSortSubtitle(sortBy: HotelSortBy) =
        when (sortBy) {
            HotelSortBy.NAME -> R.string.sort_by_hotel_name
            HotelSortBy.PRICE -> R.string.sort_by_price
            HotelSortBy.GUEST_RATING -> R.string.sort_by_guest_rating
        }

    private fun selectSortMenuItem(sortBy: HotelSortBy) {
        val menuId = when (sortBy) {
            HotelSortBy.NAME -> R.id.action_sort_by_hotel_name
            HotelSortBy.PRICE -> R.id.action_sort_by_price
            HotelSortBy.GUEST_RATING -> R.id.action_sort_by_guest_rating
        }
        menuId.let {
            menu?.findItem(it)?.isChecked = true
        }
    }

    companion object {
        const val BUNDLE_KEY_INITIALIZED = "initialized"

        fun newInstance(): ListFragment =
            ListFragment()
    }
}
