package com.currenjin.saga.choreography.travel

import com.currenjin.saga.core.SagaEventBus

class TravelBookingSaga(private val eventBus: SagaEventBus) {

    private val flightService = FlightService(eventBus)
    private val hotelService = HotelService(eventBus)
    private val carRentalService = CarRentalService(eventBus)

    fun setup() {
        flightService.register()
        hotelService.register()
        carRentalService.register()

        eventBus.subscribe(TravelEventTypes.FLIGHT_BOOKED) { event ->
            eventBus.publish(TravelEventTypes.BOOK_HOTEL, event)
        }

        eventBus.subscribe(TravelEventTypes.HOTEL_BOOKED) { event ->
            eventBus.publish(TravelEventTypes.BOOK_CAR_RENTAL, event)
        }

        eventBus.subscribe(TravelEventTypes.HOTEL_BOOKING_FAILED) { event ->
            eventBus.publish(TravelEventTypes.CANCEL_FLIGHT, event)
        }

        eventBus.subscribe(TravelEventTypes.CAR_RENTAL_BOOKING_FAILED) { event ->
            eventBus.publish(TravelEventTypes.CANCEL_HOTEL, event)
            eventBus.publish(TravelEventTypes.CANCEL_FLIGHT, event)
        }
    }

    fun start(context: TravelBookingContext) {
        eventBus.publish(TravelEventTypes.BOOK_FLIGHT, context)
    }
}
