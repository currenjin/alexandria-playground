package com.currenjin.saga.choreography.travel

import com.currenjin.saga.core.SagaEventBus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TravelBookingSagaTest {

    private lateinit var eventBus: SagaEventBus
    private lateinit var saga: TravelBookingSaga

    @BeforeEach
    fun setUp() {
        eventBus = SagaEventBus()
        saga = TravelBookingSaga(eventBus)
        saga.setup()
    }

    @Test
    fun `전체 여행 예약이 성공한다`() {
        val context = TravelBookingContext(bookingId = "TRV-001")

        saga.start(context)

        assertThat(context.flightBooked).isTrue()
        assertThat(context.hotelBooked).isTrue()
        assertThat(context.carRentalBooked).isTrue()
    }

    @Test
    fun `호텔 예약 실패 시 항공편 취소 보상이 수행된다`() {
        val context = TravelBookingContext(
            bookingId = "TRV-002",
            shouldFailAt = "호텔 예약",
        )

        saga.start(context)

        assertThat(context.flightBooked).isFalse()
        assertThat(context.flightCancelled).isTrue()
        assertThat(context.hotelBooked).isFalse()
        assertThat(context.carRentalBooked).isFalse()
    }

    @Test
    fun `렌터카 실패 시 호텔과 항공편 취소 보상이 수행된다`() {
        val context = TravelBookingContext(
            bookingId = "TRV-003",
            shouldFailAt = "렌터카 예약",
        )

        saga.start(context)

        assertThat(context.flightBooked).isFalse()
        assertThat(context.flightCancelled).isTrue()
        assertThat(context.hotelBooked).isFalse()
        assertThat(context.hotelCancelled).isTrue()
        assertThat(context.carRentalBooked).isFalse()
    }
}
