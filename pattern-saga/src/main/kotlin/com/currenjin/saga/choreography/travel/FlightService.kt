package com.currenjin.saga.choreography.travel

import com.currenjin.saga.core.SagaEventBus

class FlightService(private val eventBus: SagaEventBus) {

    fun register() {
        eventBus.subscribe(TravelEventTypes.BOOK_FLIGHT) { event ->
            val context = event as TravelBookingContext
            bookFlight(context)
        }
        eventBus.subscribe(TravelEventTypes.CANCEL_FLIGHT) { event ->
            val context = event as TravelBookingContext
            cancelFlight(context)
        }
    }

    private fun bookFlight(context: TravelBookingContext) {
        if (context.shouldFailAt == "항공편 예약") {
            eventBus.publish(TravelEventTypes.FLIGHT_BOOKING_FAILED, context)
            return
        }
        context.flightBooked = true
        eventBus.publish(TravelEventTypes.FLIGHT_BOOKED, context)
    }

    private fun cancelFlight(context: TravelBookingContext) {
        context.flightBooked = false
        context.flightCancelled = true
    }
}
