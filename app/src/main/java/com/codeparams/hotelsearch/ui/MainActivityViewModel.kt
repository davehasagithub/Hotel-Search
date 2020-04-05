package com.codeparams.hotelsearch.ui

import android.content.Context
import androidx.arch.core.util.Function
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.codeparams.hotelsearch.data.HotelDb
import com.codeparams.hotelsearch.data.HotelRepository
import com.codeparams.hotelsearch.data.HotelSortBy
import com.codeparams.hotelsearch.util.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject
internal constructor(val context: Context, val hotelRepository: HotelRepository) : ViewModel() {

    // TODO add LiveData to track state while retrieving hotels
    // TODO retry button on failed load
    // TODO don't reload hotels unless stale
    // TODO swipe-down to refresh
    // TODO placeholder items while loading hotels
    // TODO show swipe-down spinner during initial load

    var navLocation = NAV_LOCATION_LIST
        private set

    private val _sortByLiveData = MutableLiveData<HotelSortBy>()
    private val _toggleViewLiveData = MutableLiveData<Event<Int>>()
    private val _hotelsLiveData = Transformations.switchMap(_sortByLiveData, HotelsSwitchMap())
    private val _hotelsForMapLiveData = hotelRepository.getHotelsForMap()

    val sortByLiveData: LiveData<HotelSortBy>?
        get() = _sortByLiveData

    val sortBy: HotelSortBy
        get() = _sortByLiveData.value ?: HotelSortBy.DEFAULT

    val toggleViewLiveData: LiveData<Event<Int>>
        get() = _toggleViewLiveData

    val hotelsLiveData: LiveData<PagedList<HotelDb>>?
        get() = _hotelsLiveData

    val hotelsForMapLiveData: LiveData<List<HotelDb>>?
        get() = _hotelsForMapLiveData

    init {
        viewModelScope.launch {
            hotelRepository.loadHotels(context)
        }
    }

    fun init(sortBy: HotelSortBy?, navLocation: Int?) {
        if (_sortByLiveData.value == null) {
            this.navLocation = navLocation ?: NAV_LOCATION_LIST
            sortBy?.let { changeSort(it) }
        }
    }

    private inner class HotelsSwitchMap : Function<HotelSortBy, LiveData<PagedList<HotelDb>>> {
        override fun apply(sortBy: HotelSortBy): LiveData<PagedList<HotelDb>> =
            hotelRepository.getHotels(sortBy)
    }

    fun changeSort(sort: HotelSortBy) {
        _sortByLiveData.value = sort
    }

    fun toggleView() {
        navLocation = if (navLocation == NAV_LOCATION_LIST) NAV_LOCATION_MAP else NAV_LOCATION_LIST
        _toggleViewLiveData.value = Event(navLocation)
    }

    companion object {
        const val NAV_LOCATION_LIST = 1
        const val NAV_LOCATION_MAP = 2
    }
}
