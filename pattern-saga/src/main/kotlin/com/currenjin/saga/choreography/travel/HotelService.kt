package com.currenjin.saga.choreography.travel

import com.currenjin.saga.core.SagaEventBus

class HotelService(private val eventBus: SagaEventBus) {

    fun register() {
        eventBus.subscribe(TravelEventTypes.BOOK_HOTEL) { event ->
            val context = event as TravelBookingContext
            bookHotel(context)
        }
        eventBus.subscribe(TravelEventTypes.CANCEL_HOTEL) { event ->
            val context = event as TravelBookingContext
            cancelHotel(context)
        }
    }

    private fun bookHotel(context: TravelBookingContext) {
        if (context.shouldFailAt == "호텔 예약") {
            eventBus.publish(TravelEventTypes.HOTEL_BOOKING_FAILED, context)
            return
        }
        context.hotelBooked = true
        eventBus.publish(TravelEventTypes.HOTEL_BOOKED, context)
    }

    private fun cancelHotel(context: TravelBookingContext) {
        context.hotelBooked = false
        context.hotelCancelled = true
    }
}
