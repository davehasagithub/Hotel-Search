package com.codeparams.hotelsearch.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
abstract class HotelDao {

    @Query("SELECT * FROM hotel limit 100")
    abstract fun getHotelsForMap(): LiveData<List<HotelDb>>

    @Query("SELECT * FROM hotel ORDER BY name")
    abstract fun getAllHotelsByName(): DataSource.Factory<Int, HotelDb>

    @Query("SELECT * FROM hotel ORDER BY price DESC")
    abstract fun getAllHotelsByPrice(): DataSource.Factory<Int, HotelDb>

    @Query("SELECT * FROM hotel ORDER BY guest_rating DESC")
    abstract fun getAllHotelsByGuestRating(): DataSource.Factory<Int, HotelDb>

    @Query("delete from hotel")
    abstract fun deleteAllHotels()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(obj: HotelDb): Long

    @Transaction
    open fun saveHotels(hotels: List<HotelDb>) {
        for (hotel in hotels) {
            insert(hotel)
        }
    }
}
