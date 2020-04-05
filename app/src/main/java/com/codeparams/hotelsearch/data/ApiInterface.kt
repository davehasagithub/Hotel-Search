package com.codeparams.hotelsearch.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {

    // paths comes from strings.xml https://stackoverflow.com/a/32559579

    @GET
    suspend fun getHotels(@Url url: String): Response<HotelRemoteWrapper>

}
