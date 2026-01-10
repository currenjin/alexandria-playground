package com.currenjin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class BillingCycleMonthlyTest {
    @Test
    fun shouldReturnFirstBillingDateWhenTodayBeforeDayOfMonth() {
        val cycle = BillingCycle.Monthly(dayOfMonth = 15)
        val firstBillingDate = LocalDate.of(2026, 1, 15)
        val today = LocalDate.of(2026, 1, 10)

        val nextBillingDate = cycle.nextBillingDate(firstBillingDate, today)

        assertThat(nextBillingDate).isEqualTo(firstBillingDate)
    }

    @Test
    fun shouldReturnFirstBillingDateWhenTodayIsDayOfMonth() {
        val cycle = BillingCycle.Monthly(dayOfMonth = 15)
        val firstBillingDate = LocalDate.of(2026, 1, 15)
        val today = LocalDate.of(2026, 1, 15)

        val nextBillingDate = cycle.nextBillingDate(firstBillingDate, today)

        assertThat(nextBillingDate).isEqualTo(firstBillingDate)
    }

    @Test
    fun shouldReturnNextMonthBillingDateWhenTodayIsAfterDayOfMonth() {
        val cycle = BillingCycle.Monthly(dayOfMonth = 15)
        val today = LocalDate.of(2026, 1, 16)

        val nextBillingDate = cycle.nextBillingDate(LocalDate.of(2026, 1, 15), today)

        assertThat(nextBillingDate).isEqualTo(LocalDate.of(2026, 2, 15))
    }
}
