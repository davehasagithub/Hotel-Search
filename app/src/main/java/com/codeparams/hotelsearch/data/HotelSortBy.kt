package com.codeparams.hotelsearch.data

enum class HotelSortBy {
    NAME,
    PRICE,
    GUEST_RATING;

    companion object {
        val DEFAULT = NAME
    }
}
