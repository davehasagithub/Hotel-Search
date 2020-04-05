package com.codeparams.hotelsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [HotelDb::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DbTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hotelDao(): HotelDao

    companion object {
        const val DATABASE_NAME = "hotels"
    }
}
