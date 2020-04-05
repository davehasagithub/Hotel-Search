package com.codeparams.hotelsearch.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

@Entity(
    tableName = "hotel",
    indices = [Index(value = ["id"], unique = true)]
)
data class HotelDb(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "image_url")
    var imageURL: String? = null,

    @ColumnInfo(name = "star_rating")
    var starRating: BigDecimal,

    @ColumnInfo(name = "guest_rating")
    var guestRating: BigDecimal,

    @ColumnInfo(name = "price")
    var price: BigDecimal,

    @ColumnInfo(name = "discount_message")
    var discountMessage: String? = null,

    @ColumnInfo(name = "loyalty_points_earned")
    var loyaltyPointsEarned: Int,

    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "longitude")
    var longitude: Double
) : Parcelable {

    constructor(hotelRemote: HotelRemote) : this(
        0,
        hotelRemote.name,
        hotelRemote.description,
        hotelRemote.imageURL,
        DbTypeConverters.bigDecimalFromString(hotelRemote.starRating),
        DbTypeConverters.bigDecimalFromString(hotelRemote.guestRating),
        DbTypeConverters.bigDecimalFromString(hotelRemote.price),
        hotelRemote.discountMessage,
        hotelRemote.loyaltyPointsEarned?.toInt() ?: 0,
        hotelRemote.latitude?.toDouble() ?: 0.0,
        hotelRemote.longitude?.toDouble() ?: 0.0
    )

    constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        name = parcel.readString(),
        description = parcel.readString(),
        imageURL = parcel.readString(),
        starRating = DbTypeConverters.bigDecimalFromString(parcel.readString()),
        guestRating = DbTypeConverters.bigDecimalFromString(parcel.readString()),
        price = DbTypeConverters.bigDecimalFromString(parcel.readString()),
        discountMessage = parcel.readString(),
        loyaltyPointsEarned = parcel.readInt(),
        latitude = parcel.readDouble(),
        longitude = parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(imageURL)
        parcel.writeString(DbTypeConverters.bigDecimalToString(starRating))
        parcel.writeString(DbTypeConverters.bigDecimalToString(guestRating))
        parcel.writeString(DbTypeConverters.bigDecimalToString(price))
        parcel.writeString(discountMessage)
        parcel.writeInt(loyaltyPointsEarned)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<HotelDb> = object : Parcelable.Creator<HotelDb> {
            override fun createFromParcel(parcel: Parcel) = HotelDb(parcel)
            override fun newArray(size: Int) = arrayOfNulls<HotelDb>(size)
        }

        fun getHotelList(hotelRemotes: List<HotelRemote>): List<HotelDb> {
            val hotelDbs = ArrayList<HotelDb>()
            for (hotel in hotelRemotes) {
                hotelDbs.add(HotelDb(hotel))
            }
            return hotelDbs
        }
    }
}
