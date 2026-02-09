package com.currenjin.saga.choreography.travel

data class TravelBookingContext(
    val bookingId: String,
    var flightBooked: Boolean = false,
    var hotelBooked: Boolean = false,
    var carRentalBooked: Boolean = false,
    var flightCancelled: Boolean = false,
    var hotelCancelled: Boolean = false,
    var carRentalCancelled: Boolean = false,
    var shouldFailAt: String? = null,
)

object TravelEventTypes {
    const val BOOK_FLIGHT = "BOOK_FLIGHT"
    const val FLIGHT_BOOKED = "FLIGHT_BOOKED"
    const val FLIGHT_BOOKING_FAILED = "FLIGHT_BOOKING_FAILED"
    const val BOOK_HOTEL = "BOOK_HOTEL"
    const val HOTEL_BOOKED = "HOTEL_BOOKED"
    const val HOTEL_BOOKING_FAILED = "HOTEL_BOOKING_FAILED"
    const val BOOK_CAR_RENTAL = "BOOK_CAR_RENTAL"
    const val CAR_RENTAL_BOOKED = "CAR_RENTAL_BOOKED"
    const val CAR_RENTAL_BOOKING_FAILED = "CAR_RENTAL_BOOKING_FAILED"
    const val CANCEL_FLIGHT = "CANCEL_FLIGHT"
    const val CANCEL_HOTEL = "CANCEL_HOTEL"
}
