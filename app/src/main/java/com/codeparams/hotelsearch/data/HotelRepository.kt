package com.codeparams.hotelsearch.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedList.Config
import com.codeparams.hotelsearch.R
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelRepository @Inject
constructor(
    private val appDatabase: AppDatabase,
    private val apiInterface: ApiInterface
) {

    fun getHotelsForMap() =
        appDatabase.hotelDao().getHotelsForMap() // non-paging and limited to 100

    fun getHotels(sortBy: HotelSortBy): LiveData<PagedList<HotelDb>> {
        val dataSourceFactory = when (sortBy) {
            HotelSortBy.NAME -> appDatabase.hotelDao().getAllHotelsByName()
            HotelSortBy.PRICE -> appDatabase.hotelDao().getAllHotelsByPrice()
            HotelSortBy.GUEST_RATING -> appDatabase.hotelDao().getAllHotelsByGuestRating()
        }

        val config = Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()

        return LivePagedListBuilder(dataSourceFactory, config).build()
    }

    suspend fun loadHotels(context: Context) {
        deleteAllHotels()
        retrieveHotels(context)?.body()?.let {
            it.hotels?.let { hotels -> saveHotels(hotels) }
        }
    }

    private suspend fun deleteAllHotels() = withContext(IO) {
        appDatabase.hotelDao().deleteAllHotels()
    }

    private suspend fun saveHotels(hotels: List<HotelRemote>) = withContext(IO) {
        appDatabase.hotelDao().saveHotels(HotelDb.getHotelList(hotels))
    }

    private suspend fun retrieveHotels(context: Context) =
        try {
            apiInterface.getHotels(context.getString(R.string.service_endpoint_hotel_list))
        } catch (e: IOException) {
            null
        }

    companion object {
        const val PAGE_SIZE = 20
    }
}
