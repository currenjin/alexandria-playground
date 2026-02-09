package com.currenjin.saga.choreography.travel

import com.currenjin.saga.core.SagaEventBus

class CarRentalService(private val eventBus: SagaEventBus) {

    fun register() {
        eventBus.subscribe(TravelEventTypes.BOOK_CAR_RENTAL) { event ->
            val context = event as TravelBookingContext
            bookCarRental(context)
        }
    }

    private fun bookCarRental(context: TravelBookingContext) {
        if (context.shouldFailAt == "렌터카 예약") {
            eventBus.publish(TravelEventTypes.CAR_RENTAL_BOOKING_FAILED, context)
            return
        }
        context.carRentalBooked = true
        eventBus.publish(TravelEventTypes.CAR_RENTAL_BOOKED, context)
    }
}
