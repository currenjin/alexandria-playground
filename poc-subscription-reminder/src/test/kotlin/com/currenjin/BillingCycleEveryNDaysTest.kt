package com.currenjin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class BillingCycleEveryNDaysTest {
    @Test
    fun shouldReturnNextDateBasedOnIntervalWhenTodayBetweenCycles() {
        val cycle = BillingCycle.EveryNDays(days = 30)
        val firstBillingDate = LocalDate.of(2026, 1, 1)
        val today = LocalDate.of(2026, 1, 10)

        val nextBillingDate = cycle.nextBillingDate(firstBillingDate, today)

        assertThat(nextBillingDate).isEqualTo(LocalDate.of(2026, 1, 31))
    }

    @Test
    fun shouldReturnSameDateWhenTodayIsOnCycle() {
        val cycle = BillingCycle.EveryNDays(days = 30)
        val firstBillingDate = LocalDate.of(2026, 1, 1)
        val today = LocalDate.of(2026, 1, 31)

        val nextBillingDate = cycle.nextBillingDate(firstBillingDate, today)

        assertThat(nextBillingDate).isEqualTo(LocalDate.of(2026, 1, 31))
    }

    @Test
    fun shouldReturnLaterDateWhenTodayPastCycle() {
        val cycle = BillingCycle.EveryNDays(days = 30)
        val firstBillingDate = LocalDate.of(2026, 1, 1)
        val today = LocalDate.of(2026, 2, 1)

        val nextBillingDate = cycle.nextBillingDate(firstBillingDate, today)

        assertThat(nextBillingDate).isEqualTo(LocalDate.of(2026, 3, 2))
    }
}
