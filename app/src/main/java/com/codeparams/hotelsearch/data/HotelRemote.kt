package com.codeparams.hotelsearch.data

import com.squareup.moshi.Json

class HotelRemote {

    @field:Json(name = "hotelName")
    var name: String? = null

    @field:Json(name = "description")
    var description: String? = null

    @field:Json(name = "hotelImageURL")
    var imageURL: String? = null

    @field:Json(name = "starRating")
    var starRating: String? = null

    @field:Json(name = "guestRating")
    var guestRating: String? = null

    @field:Json(name = "price")
    var price: String? = null

    @field:Json(name = "discountMessage")
    var discountMessage: String? = null

    @field:Json(name = "loyaltyPointsEarned")
    var loyaltyPointsEarned: String? = null

    @field:Json(name = "latitude")
    var latitude: String? = null

    @field:Json(name = "longitude")
    var longitude: String? = null

}

class HotelRemoteWrapper {
    @field:Json(name = "hotels")
    var hotels: List<HotelRemote>? = null
}