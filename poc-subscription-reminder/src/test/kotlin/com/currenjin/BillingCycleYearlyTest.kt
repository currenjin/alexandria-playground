package com.currenjin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class BillingCycleYearlyTest {
    @Test
    fun shouldReturnSameYearDateWhenTodayBeforeMonthDay() {
        val cycle = BillingCycle.Yearly(month = 6, dayOfMonth = 10)
        val firstBillingDate = LocalDate.of(2020, 6, 10)
        val today = LocalDate.of(2026, 1, 10)

        val nextBillingDate = cycle.nextBillingDate(firstBillingDate, today)

        assertThat(nextBillingDate).isEqualTo(LocalDate.of(2026, 6, 10))
    }

    @Test
    fun shouldReturnSameYearDateWhenTodayIsMonthDay() {
        val cycle = BillingCycle.Yearly(month = 6, dayOfMonth = 10)
        val firstBillingDate = LocalDate.of(2020, 6, 10)
        val today = LocalDate.of(2026, 6, 10)

        val nextBillingDate = cycle.nextBillingDate(firstBillingDate, today)

        assertThat(nextBillingDate).isEqualTo(LocalDate.of(2026, 6, 10))
    }

    @Test
    fun shouldReturnNextYearDateWhenTodayAfterMonthDay() {
        val cycle = BillingCycle.Yearly(month = 6, dayOfMonth = 10)
        val today = LocalDate.of(2026, 6, 11)

        val nextBillingDate = cycle.nextBillingDate(LocalDate.of(2020, 6, 10), today)

        assertThat(nextBillingDate).isEqualTo(LocalDate.of(2027, 6, 10))
    }

    @Test
    fun shouldClampToLastDayWhenYearHasNoDayOfMonth() {
        val cycle = BillingCycle.Yearly(month = 2, dayOfMonth = 29)
        val today = LocalDate.of(2026, 1, 10)

        val nextBillingDate = cycle.nextBillingDate(LocalDate.of(2024, 2, 29), today)

        assertThat(nextBillingDate).isEqualTo(LocalDate.of(2026, 2, 28))
    }
}
